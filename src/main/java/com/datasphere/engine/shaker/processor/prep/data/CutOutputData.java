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


public class CutOutputData {
	String table1id;
	int table1rows;
	String table2id;
	int table2rows;
	int totalrows;
	public int getTotalrows() {
		return totalrows;
	}
	public void setTotalrows(int totalrows) {
		this.totalrows = totalrows;
	}
	public String getTable1id() {
		return table1id;
	}
	public void setTable1id(String table1id) {
		this.table1id = table1id;
	}
	public int getTable1rows() {
		return table1rows;
	}
	public void setTable1rows(int table1rows) {
		this.table1rows = table1rows;
	}
	public String getTable2id() {
		return table2id;
	}
	public void setTable2id(String table2id) {
		this.table2id = table2id;
	}
	public int getTable2rows() {
		return table2rows;
	}
	public void setTable2rows(int table2rows) {
		this.table2rows = table2rows;
	}
	
}
