package com.datasphere.engine.shaker.processor.prep.data;

import java.util.Date;

//@Table(name = "T_OPERATE_COLUMNS")
public class OperateColumnData {

//	@Id
	//@javax.persistence.Column(name = "ID")
	private Integer id;
	
    //@javax.persistence.Column(name = "COLUMN_ID")
    private Integer columnId;

    //@javax.persistence.Column(name = "NAME")
    private String name;

    //@javax.persistence.Column(name = "REAL_NAME")
    private String realName;

    //@javax.persistence.Column(name = "TYPE")
    private String type;

    //@javax.persistence.Column(name = "SOURCE_FLAG")
    private String sourceFlag;

    //@javax.persistence.Column(name = "TABLE_ID")
    private String tableId;

    //@javax.persistence.Column(name = "PROCESS_ID")
    private String processId;

    //@javax.persistence.Column(name = "STATUS")
    private String status;

    //@javax.persistence.Column(name = "CHART_BAR")
    private String chartBar;

    //@javax.persistence.Column(name = "CHART_DISTRIBUTION")
    private String chartDistribution;

    //@javax.persistence.Column(name = "DATA_SUMMARY")
    private String dataSummary;

    //@javax.persistence.Column(name = "DATA_STATISTICS")
    private String dataStatistics;

    //@javax.persistence.Column(name = "COLUMN_ORDER")
    private Integer columnOrder;

    //@javax.persistence.Column(name = "PREVIEW_DATA")
    private String previewData;

    //@javax.persistence.Column(name = "UPDATE_TIME")
    private Date updateTime;

    //@javax.persistence.Column(name = "CREATE_TIME")
    private Date createTime;

    public OperateColumnData() {
    }

    public OperateColumnData(Integer id, String name, String realName, String type, String sourceFlag, String tableId, String processId, String status, String chartBar, String chartDistribution, String dataSummary, String dataStatistics, Integer columnOrder, String previewData, Date updateTime, Date createTime) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.type = type;
        this.sourceFlag = sourceFlag;
        this.tableId = tableId;
        this.processId = processId;
        this.status = status;
        this.chartBar = chartBar;
        this.chartDistribution = chartDistribution;
        this.dataSummary = dataSummary;
        this.dataStatistics = dataStatistics;
        this.columnOrder = columnOrder;
        this.previewData = previewData;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(String sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId == null ? null : processId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChartBar() {
        return chartBar;
    }

    public void setChartBar(String chartBar) {
        this.chartBar = chartBar;
    }

    public String getChartDistribution() {
        return chartDistribution;
    }

    public void setChartDistribution(String chartDistribution) {
        this.chartDistribution = chartDistribution;
    }

    public String getDataSummary() {
        return dataSummary;
    }

    public void setDataSummary(String dataSummary) {
        this.dataSummary = dataSummary;
    }

    public String getDataStatistics() {
        return dataStatistics;
    }

    public void setDataStatistics(String dataStatistics) {
        this.dataStatistics = dataStatistics;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    public String getPreviewData() {
        return previewData;
    }

    public void setPreviewData(String previewData) {
        this.previewData = previewData;
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

    public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	@Override
    public String toString() {
        return "ColumnData{" +
                "id=" + id +
                "columnId=" + columnId +
                ", name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", type='" + type + '\'' +
                ", sourceFlag='" + sourceFlag + '\'' +
                ", tableId='" + tableId + '\'' +
                ", status='" + status + '\'' +
                ", chartBar='" + chartBar + '\'' +
                ", chartDistribution='" + chartDistribution + '\'' +
                ", dataSummary='" + dataSummary + '\'' +
                ", dataStatistics='" + dataStatistics + '\'' +
                ", columnOrder=" + columnOrder +
                ", previewData='" + previewData + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}
