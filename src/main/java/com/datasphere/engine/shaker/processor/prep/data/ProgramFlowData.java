package com.datasphere.engine.shaker.processor.prep.data;

import com.datasphere.common.dmpbase.data.ParamsVO;
import com.datasphere.engine.shaker.processor.prep.domain.Column;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjt on 2017/7/10.
 */


public class ProgramFlowData {

    // 方案id
    private String processId;

    // 方案id
    private Integer programId;

    // 方案的输出列
    private List<Column> columns;

    // 方案的输出操作
    private List<ParamsVO> operates;


    public ProgramFlowData() {
    }

    public ProgramFlowData(String processId, Integer programId, List<Column> columns, List<ParamsVO> operates) {
        this.processId = processId;
        this.programId = programId;
        this.columns = columns;
        this.operates = operates;
    }

    public ProgramFlowData(ProgramOutputData programOutputData) {
        List<ParamsVO> paramsVOS = new ArrayList<>();
        this.processId = programOutputData.getProcessId();
        this.columns = programOutputData.getColumns();
        this.programId = programOutputData.getProgramId();


        if (programOutputData.getOperates() != null && programOutputData.getOperates().size() > 0) {
            Gson gson = new GsonBuilder().create();
            for (OperateData operateData : programOutputData.getOperates()) {
                assert StringUtils.isNotEmpty(operateData.getParamsvo());
                ParamsVO paramsVO = gson.fromJson(operateData.getParamsvo(), ParamsVO.class);
                paramsVOS.add(paramsVO);
            }
            this.operates = paramsVOS;
        }
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

    public List<ParamsVO> getOperates() {
        return operates;
    }

    public void setOperates(List<ParamsVO> operates) {
        this.operates = operates;
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
