package com.datasphere.engine.manager.resource.provider.model;


import java.util.Date;

import com.datasphere.engine.datasource.mybatis.page.Pager;

public class DataSource {
	private String id;

	//数据源名称
	private String name;

	//分类 -001数据源/002...
	private String classification;

	//数据源描述
	private  String dataDesc;

	//数据源大小
	private Long dataSize;

	//数据源文件类型（csv和arff）)
	private String dataFileType;

	//文件上传路径
	private String dataFileUrl;

	//文件原名称
	private String dataFileName;

	private String code;

	//文件多少行

	private String dataPerform;
	//文件多少列

	private String dataColumns;
	//文件字段类型

	private Object dataColumnsType;
	//文件预览100行数据

	private Object dataFilePart;
	//数据类型：0：文件上传；1：数据库上传

	private Integer dataType;
	//数据库配置

	private String dataConfig;
	//数据库上传状态

	private Integer dataState;
	//数据源上传状态信息

	private String dataFileEncode;
	//数据源上传状态信息

	private String dataMessage;
	//csv文件是否有表头：0：有表头；1 没有表头

	private String dataHead;
	private String creator;

	//	@JsonInclude(value=Include.NON_NULL)

	private Date createTime;
	//	@JsonInclude(value=Include.NON_NULL)

	protected Date lastModified;
	//	@JsonInclude(value=Include.NON_NULL)

	Pager pager;
	//排序字段

	//	@JsonProperty(access=Access.WRITE_ONLY)
	private String sortField;
	//升序降序

	//	@JsonProperty(access=Access.WRITE_ONLY)
	private String orderBy;



	//数据库类型（mysql，oracle，postgres...）

	private String dataDSType;
	//业务类型

	private String businessType;
	//文件来源

	private String dataFrom;

	//被引用次数
	private Integer dataQuote;

	//url连接ip
	private String connectionURL;

	//判断是否为整库导入
	private String dataSourceDemension;

	private String departmentId;
	private String userId;
	private String dataCode;

	private String dataBaseAlias;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}

	public Long getDataSize() {
		return dataSize;
	}

	public void setDataSize(Long dataSize) {
		this.dataSize = dataSize;
	}

	public String getDataFileType() {
		return dataFileType;
	}

	public void setDataFileType(String dataFileType) {
		this.dataFileType = dataFileType;
	}

	public String getDataFileUrl() {
		return dataFileUrl;
	}

	public void setDataFileUrl(String dataFileUrl) {
		this.dataFileUrl = dataFileUrl;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getDataPerform() {
		return dataPerform;
	}

	public void setDataPerform(String dataPerform) {
		this.dataPerform = dataPerform;
	}

	public String getDataColumns() {
		return dataColumns;
	}

	public void setDataColumns(String dataColumns) {
		this.dataColumns = dataColumns;
	}

	public Object getDataColumnsType() {
		return dataColumnsType;
	}

	public void setDataColumnsType(Object dataColumnsType) {
		this.dataColumnsType = dataColumnsType;
	}

	public Object getDataFilePart() {
		return dataFilePart;
	}

	public void setDataFilePart(Object dataFilePart) {
		this.dataFilePart = dataFilePart;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getDataConfig() {
		return dataConfig;
	}

	public void setDataConfig(String dataConfig) {
		this.dataConfig = dataConfig;
	}

	public Integer getDataState() {
		return dataState;
	}

	public void setDataState(Integer dataState) {
		this.dataState = dataState;
	}

	public String getDataFileEncode() {
		return dataFileEncode;
	}

	public void setDataFileEncode(String dataFileEncode) {
		this.dataFileEncode = dataFileEncode;
	}

	public String getDataMessage() {
		return dataMessage;
	}

	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}

	public String getDataHead() {
		return dataHead;
	}

	public void setDataHead(String dataHead) {
		this.dataHead = dataHead;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getDataDSType() {
		return dataDSType;
	}

	public void setDataDSType(String dataDSType) {
		this.dataDSType = dataDSType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Integer getDataQuote() {
		return dataQuote;
	}

	public void setDataQuote(Integer dataQuote) {
		this.dataQuote = dataQuote;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getDataSourceDemension() {
		return dataSourceDemension;
	}

	public void setDataSourceDemension(String dataSourceDemension) {
		this.dataSourceDemension = dataSourceDemension;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataBaseAlias() {
		return dataBaseAlias;
	}

	public void setDataBaseAlias(String dataBaseAlias) {
		this.dataBaseAlias = dataBaseAlias;
	}
}
