package com.datasphere.common.data;

public class Column {

//	String columnName;
//	String columnType;
//
//	public String getColumnName() {
//		return columnName;
//	}
//
//	public void setColumnName(String columnName) {
//		this.columnName = columnName;
//	}
//
//	public String getColumnType() {
//		return columnType;
//	}
//
//	public void setColumnType(String columnType) {
//		this.columnType = columnType;
//	}

	/**
	 * 列的原名称
	 */
	String name;
	
	/**
	 * 列的JDBC类型编码，参考{@link java.sql.Types}
	 */
	Integer sourceType;
	
	/**
	 * 列的业务类型
	 */
	String type;
	
	/**
	 * 保留字段
	 */
	String reserve;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
