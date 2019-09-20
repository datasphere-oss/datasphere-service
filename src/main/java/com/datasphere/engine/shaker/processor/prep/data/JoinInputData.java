/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.shaker.processor.prep.data;

import java.util.List;

/**
 * 连接表
 */
public class JoinInputData {
	String table1id;//数据集1datakey
	String table2id;//数据集2datakey

	String joinType="1";//ty

	List<String>table1ColumnList;//输出列1
	List<String>table2ColumnList;//输出列2
	List<JoinInputColumn> joinColumnList;

	public String getTable1id() {
		return table1id;
	}
	public void setTable1id(String table1id) {
		this.table1id = table1id;
	}
	public String getTable2id() {
		return table2id;
	}
	public void setTable2id(String table2id) {
		this.table2id = table2id;
	}
	public List<String> getTable1ColumnList() {
		return table1ColumnList;
	}
	public void setTable1ColumnList(List<String> table1ColumnList) {
		this.table1ColumnList = table1ColumnList;
	}
	public List<String> getTable2ColumnList() {
		return table2ColumnList;
	}
	public void setTable2ColumnList(List<String> table2ColumnList) {
		this.table2ColumnList = table2ColumnList;
	}
	public List<JoinInputColumn> getJoinColumnList() {
		return joinColumnList;
	}
	public void setJoinColumnList(List<JoinInputColumn> joinColumnList) {
		this.joinColumnList = joinColumnList;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	
}
