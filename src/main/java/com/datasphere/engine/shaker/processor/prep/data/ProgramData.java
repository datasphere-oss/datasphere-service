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

//import javax.persistence.Column;
//import javax.persistence.Id;
//import javax.persistence.Table;
import java.util.Date;
import java.util.List;

//@Table(name = "T_PROGRAMS")
public class ProgramData {

//    @Id
    //@javax.persistence.Column(name = "ID")
    private Integer id;

    //@javax.persistence.Column(name = "NAME")
    private String name;

    //@javax.persistence.Column(name = "IS_DEFAULT")
    private String isDefault;

    //@javax.persistence.Column(name = "VERSION")
    private Integer version;

    //@javax.persistence.Column(name = "PROCESS_ID")
    private String processId;

    private List<ProgramColumnData> columns;

    private List<OperateData> operates;

//    @Column(name = "RESULT")
    private String result;

    //@javax.persistence.Column(name = "TABLE_ID")
    private String tableId;

    //@javax.persistence.Column(name = "UPDATE_TIME")
    private Date updateTime;

    //@javax.persistence.Column(name = "CREATE_TIME")
    private Date createTime;

    public ProgramData() {
    }

    public ProgramData(Integer id, String name, String isDefault, Integer version, String processId, List<ProgramColumnData> columns, List<OperateData> operates, String result, String tableId, Date updateTime, Date createTime) {
        this.id = id;
        this.name = name;
        this.isDefault = isDefault;
        this.version = version;
        this.processId = processId;
        this.columns = columns;
        this.operates = operates;
        this.result = result;
        this.tableId = tableId;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<ProgramColumnData> getColumns() {
        return columns;
    }

    public void setColumns(List<ProgramColumnData> columns) {
        this.columns = columns;
    }

    public List<OperateData> getOperates() {
        return operates;
    }

    public void setOperates(List<OperateData> operates) {
        this.operates = operates;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ProgramData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", version=" + version +
                ", processId=" + processId +
                ", columns=" + columns +
                ", operates=" + operates +
                ", result='" + result + '\'' +
                ", tableId='" + tableId + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}