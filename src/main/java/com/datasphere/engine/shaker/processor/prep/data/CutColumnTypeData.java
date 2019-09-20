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

public class CutColumnTypeData {
	int totalColumnTypeCount=0;
	int table1CrrentColumnTypeCount=0;
	int table2CrrentColumnTypeCount=0;
	int table1ColumnTypeCount=0;
	int table2ColumnTypeCount=0;
	
	public int getTable1ColumnTypeCount() {
		return table1ColumnTypeCount;
	}
	public void setTable1ColumnTypeCount(int table1ColumnTypeCount) {
		this.table1ColumnTypeCount = table1ColumnTypeCount;
	}
	public int getTable2ColumnTypeCount() {
		return table2ColumnTypeCount;
	}
	public void setTable2ColumnTypeCount(int table2ColumnTypeCount) {
		this.table2ColumnTypeCount = table2ColumnTypeCount;
	}
	public int getTotalColumnTypeCount() {
		return totalColumnTypeCount;
	}
	public void setTotalColumnTypeCount(int totalColumnTypeCount) {
		this.totalColumnTypeCount = totalColumnTypeCount;
	}
	public int getTable1CrrentColumnTypeCount() {
		return table1CrrentColumnTypeCount;
	}
	public void setTable1CrrentColumnTypeCount(int table1CrrentColumnTypeCount) {
		this.table1CrrentColumnTypeCount = table1CrrentColumnTypeCount;
	}
	public int getTable2CrrentColumnTypeCount() {
		return table2CrrentColumnTypeCount;
	}
	public void setTable2CrrentColumnTypeCount(int table2CrrentColumnTypeCount) {
		this.table2CrrentColumnTypeCount = table2CrrentColumnTypeCount;
	}
	
}
