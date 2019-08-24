package com.datasphere.engine.shaker.processor.prep.data;

import java.util.Date;
import java.util.List;

import com.datasphere.engine.shaker.processor.prep.domain.EvaluationProgram;

//@Table(name = "T_EVALUATIONS")
public class EvaluationData {

//    @Id
    //@javax.persistence.Column(name = "ID")
    private Integer id;

    //@javax.persistence.Column(name = "STATUS")
    private String status;

    //@javax.persistence.Column(name = "ALGORITHM_CODE")
    private String algorithmCode;

    //@javax.persistence.Column(name = "METRIC_CODES")
    private String metricCodes;

    //@javax.persistence.Column(name = "PROCESS_ID")
    private String processId;

    private List<EvaluationProgram> evaluationPrograms;

    private List<ProgramData> programs;

    //@javax.persistence.Column(name = "UPDATE_TIME")
    private Date updateTime;

    //@javax.persistence.Column(name = "CREATE_TIME")
    private Date createTime;

    public EvaluationData() {
    }

    public EvaluationData(Integer id, String status, String algorithmCode, String metricCodes,
            String processId, List<EvaluationProgram> evaluationPrograms,
            List<ProgramData> programs, Date updateTime, Date createTime) {
        this.id = id;
        this.status = status;
        this.algorithmCode = algorithmCode;
        this.metricCodes = metricCodes;
        this.processId = processId;
        this.evaluationPrograms = evaluationPrograms;
        this.programs = programs;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlgorithmCode() {
        return algorithmCode;
    }

    public void setAlgorithmCode(String algorithmCode) {
        this.algorithmCode = algorithmCode;
    }

    public String getMetricCodes() {
        return metricCodes;
    }

    public void setMetricCodes(String metricCodes) {
        this.metricCodes = metricCodes;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<EvaluationProgram> getEvaluationPrograms() {
        return evaluationPrograms;
    }

    public void setEvaluationPrograms(List<EvaluationProgram> evaluationPrograms) {
        this.evaluationPrograms = evaluationPrograms;
    }

    public List<ProgramData> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramData> programs) {
        this.programs = programs;
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

    @Override public String toString() {
        return "EvaluationData{" + "id=" + id + ", status='" + status + '\'' + ", algorithmCode='"
                + algorithmCode + '\'' + ", metricCodes='" + metricCodes + '\'' + ", processId='"
                + processId + '\'' + ", evaluationPrograms=" + evaluationPrograms + ", programs="
                + programs + ", updateTime=" + updateTime + ", createTime=" + createTime + '}';
    }
}

