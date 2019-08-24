package com.datasphere.engine.shaker.processor.prep.data;

import java.util.List;

import com.datasphere.engine.shaker.processor.prep.domain.Column;

/**
 * Created by wangjt on 2017/7/26.
 */
public class ExecResultData {
    private String processId;
    private Integer programId;
    private Integer operateId;
    private String operateCode;
    private boolean success;
    private String message;
    private boolean hasNext;
    private List<Column> generatedColumns;

    public ExecResultData() {
    }

    public ExecResultData(String processId, Integer programId, Integer operateId, boolean success) {
        this.processId = processId;
        this.programId = programId;
        this.operateId = operateId;
        this.success = success;
    }

    public String getProcessId() {
        return processId;
    }

    public ExecResultData setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public Integer getProgramId() {
        return programId;
    }

    public ExecResultData setProgramId(Integer programId) {
        this.programId = programId;
        return this;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public ExecResultData setOperateId(Integer operateId) {
        this.operateId = operateId;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ExecResultData setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public List<Column> getGeneratedColumns() {
        return generatedColumns;
    }

    public ExecResultData setGeneratedColumns(List<Column> generatedColumns) {
        this.generatedColumns = generatedColumns;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ExecResultData setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public ExecResultData setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public ExecResultData setOperateCode(String operateCode) {
        this.operateCode = operateCode;
        return this;
    }

    public static void main(String[] args) {
    }
}
