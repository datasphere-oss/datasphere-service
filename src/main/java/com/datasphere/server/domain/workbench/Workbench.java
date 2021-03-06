/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.workbench;

import com.google.common.collect.Maps;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Map;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.datasphere.engine.datasource.connections.DataConnection;
import com.datasphere.server.domain.workspace.Book;
import com.datasphere.server.domain.workspace.Workspace;

@Entity
@Table(name = "book_workbench")
@JsonTypeName("workbench")
@DiscriminatorValue("workbench")
public class Workbench extends Book {
	
	// 全局命名空间
	static final String GLOBAL_VAR_KEY_NAME = "globalNm";
	// 全局类型
	static final String GLOBAL_VAR_KEY_TYPE = "globalType";
	// 全局变量
	static final String GLOBAL_VAR_KEY_VALUE = "globalVar";

	static final String GLOBAL_VAR_TYPE_TEXT = "t";
	static final String GLOBAL_VAR_TYPE_CALENDAR = "c";
	// 全局变量设置
	@Column(name = "workbench_global_var")
	@Size(max = 5000)
	String globalVar;
	// 数据库名称
	@Column(name = "database_name")
	String databaseName;
	// 查询编辑器
	@OneToMany(mappedBy = "workbench", cascade = CascadeType.ALL)
	@OrderBy("createdTime ASC")
	@RestResource(path = "queryeditors")
	Set<QueryEditor> queryEditors;
	// 数据库连接
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dc_id", referencedColumnName = "id")
	@RestResource(path = "dataconnection")
	@JsonBackReference(value = "dataconnection-workbench")
	@NotNull
	DataConnection dataConnection;

	@Override
	public Book copyOf(Workspace parent, boolean addPrefix) {
		throw new NotImplementedException("TODO");
	}

	@Override
	public Map<String, Object> listViewProjection() {
		Map<String, Object> projection = super.listViewProjection();
		projection.put("type", "workbench");
		// 连接类型、连接名称、连接校验
		Map<String, Object> contents = Maps.newLinkedHashMap();
		contents.put("connType", dataConnection.getImplementor());
		contents.put("connName", dataConnection.getName());
		contents.put("connValid", dataConnection.getWorkspaces().contains(workspace) || BooleanUtils.isTrue(dataConnection.getPublished()));
		// 连接内容
		projection.put("contents", contents);

		return projection;
	}
	// 树形投影
	@Override
	public Map<String, Object> treeViewProjection() {
		Map<String, Object> projection = super.treeViewProjection();
		projection.put("type", "workbench");

		return projection;
	}

	public String getGlobalVar() {
		return globalVar;
	}

	public void setGlobalVar(String globalVar) {
		this.globalVar = globalVar;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Set<QueryEditor> getQueryEditors() {
		return queryEditors;
	}

	public void setQueryEditors(Set<QueryEditor> queryEditors) {
		this.queryEditors = queryEditors;
	}

	public DataConnection getDataConnection() {
		return dataConnection;
	}

	public void setDataConnection(DataConnection dataConnection) {
		this.dataConnection = dataConnection;
	}

	@Override
	public String toString() {
		return "Workbench{" +
						"globalVar='" + globalVar + '\'' +
						", databaseName='" + databaseName + '\'' +
						", queryEditors=" + queryEditors +
						", dataConnection=" + dataConnection +
						", id='" + id + '\'' +
						", type='" + type + '\'' +
						", name='" + name + '\'' +
						", description='" + description + '\'' +
						", favorite=" + favorite +
						", tag='" + tag + '\'' +
						", workspace=" + workspace +
						", folderId='" + folderId + '\'' +
						", bookType='" + bookType + '\'' +
						'}';
	}
}
