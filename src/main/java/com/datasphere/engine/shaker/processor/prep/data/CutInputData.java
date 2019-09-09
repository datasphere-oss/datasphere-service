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
