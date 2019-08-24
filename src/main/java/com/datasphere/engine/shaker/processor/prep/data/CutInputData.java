package com.datasphere.engine.shaker.processor.prep.data;


/**
 * @author liming
 *
 */
public class CutInputData {
	Double cutPerset;
	 
	 String tableID;
	 
	 String splitColumnName;
	 
	public Double getCutPerset() {
		return cutPerset;
	}
	public void setCutPerset(Double cutPerset) {
		this.cutPerset = cutPerset;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public String getSplitColumnName() {
		return splitColumnName;
	}
	public void setSplitColumnName(String splitColumnName) {
		splitColumnName = splitColumnName;
	}
	 
	
}
