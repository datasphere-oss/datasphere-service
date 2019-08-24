package com.datasphere.engine.shaker.processor.prep.data;

/**
 * Created by admin on 2017/7/7.
 */
public class EvaluationResultData {

    //@javax.persistence.Column(name = "EVALUATION_ID")
    private String evaluationId;

    //@javax.persistence.Column(name = "PROCESS_ID")
    private String processId;

    //@javax.persistence.Column(name = "PROGRAM_ID")
    private String programId;

    //@javax.persistence.Column(name = "PROGRAM_NAME")
    private String programName;

    //@javax.persistence.Column(name = "RESULT")
    private String result;

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
