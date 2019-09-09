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

public class JoinInputColumn {
	String table1ColumnName;
	String table2ColumnName;
	int table1ColumnIndex=-1;
	int table2ColumnIndex=-1;
	public String getTable1ColumnName() {
		return table1ColumnName;
	}
	public void setTable1ColumnName(String table1ColumnName) {
		this.table1ColumnName = table1ColumnName;
	}
	public String getTable2ColumnName() {
		return table2ColumnName;
	}
	public void setTable2ColumnName(String table2ColumnName) {
		this.table2ColumnName = table2ColumnName;
	}
	public int getTable1ColumnIndex() {
		return table1ColumnIndex;
	}
	public void setTable1ColumnIndex(int table1ColumnIndex) {
		this.table1ColumnIndex = table1ColumnIndex;
	}
	public int getTable2ColumnIndex() {
		return table2ColumnIndex;
	}
	public void setTable2ColumnIndex(int table2ColumnIndex) {
		this.table2ColumnIndex = table2ColumnIndex;
	}
	
}
