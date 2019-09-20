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

import com.datasphere.engine.shaker.processor.prep.model.Column;

public class ProgramOutputData {
    // 方案id
    private String processId;

    // 方案id
    private Integer programId;

    // 方案的输出列
    private List<Column> columns;

    // 方案的输出操作
    private List<OperateData> operates;

    //方案中的列数
    private Integer columnCount;

    //整个数据集里的行数
    private long rowCount;


    public ProgramOutputData() {
    }

    public ProgramOutputData(String processId, Integer programId,
                             List<Column> columns, List<OperateData> operates, Integer columnCount, long rowCount) {
        this.processId = processId;
        this.programId = programId;
        this.columns = columns;
        this.operates = operates;
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<OperateData> getOperates() {
        return operates;
    }

    public void setOperates(List<OperateData> operates) {
        this.operates = operates;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public String toString() {
        return "ProgramOutputData{" +
                "processId=" + processId +
                ", programId=" + programId +
                ", columns=" + columns +
                ", operates=" + operates +
                '}';
    }
}
