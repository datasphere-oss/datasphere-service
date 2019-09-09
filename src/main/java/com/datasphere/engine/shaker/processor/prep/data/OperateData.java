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

import java.util.Date;
import java.util.List;

//@Table(name = "T_OPERATE")
public class OperateData implements Comparable<OperateData> {

//    @Id
    //@Column(name = "ID")
    private Integer id;

    private List<ColumnData> sources;

    private List<ColumnData> targets;


    /**
     * 列名 + 空格 + 操作名称
     */
    private String operateName;

    //@Column(name = "STATUS")
    private String status;

    //@Column(name = "OPERATE_CODE")
    private String operateCode;

    //@Column(name = "PARAMETERS")
    private String parameters;

    //@Column(name = "PARAMSVO")
    private String paramsvo;

    //@Column(name = "PROCESS_ID")
    private String processId;

    //@Column(name = "UPDATE_TIME")
    private Date updateTime;

    //@Column(name = "CREATE_TIME")
    private Date createTime;

    public OperateData() {
    }

    public OperateData(Integer id, List<ColumnData> sources, List<ColumnData> targets, String status, String operateCode, String parameters, String paramsvo, String processId, Date updateTime, Date createTime) {
        this.id = id;
        this.sources = sources;
        this.targets = targets;
        this.status = status;
        this.operateCode = operateCode;
        this.parameters = parameters;
        this.paramsvo = paramsvo;
        this.processId = processId;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ColumnData> getSources() {
        return sources;
    }

    public void setSources(List<ColumnData> sources) {
        this.sources = sources;
    }

    public List<ColumnData> getTargets() {
        return targets;
    }

    public void setTargets(List<ColumnData> targets) {
        this.targets = targets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParamsvo() {
        return paramsvo;
    }

    public void setParamsvo(String paramsvo) {
        this.paramsvo = paramsvo;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
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
        return "OperateData{" +
                "id=" + id +
                ", sources=" + sources +
                ", targets=" + targets +
                ", status='" + status + '\'' +
                ", operateCode='" + operateCode + '\'' +
                ", parameters='" + parameters + '\'' +
                ", paramsvo='" + paramsvo + '\'' +
                ", processId='" + processId + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    @Override
    public int compareTo(OperateData o) {
        return id < o.id ? -1 : (id == o.id ? 0 : 1);
    }
}
