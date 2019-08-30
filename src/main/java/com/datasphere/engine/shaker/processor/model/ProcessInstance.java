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

package com.datasphere.engine.shaker.processor.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class ProcessInstance {
	private String id;
	private String panelId;
	private String fromComponentInstanceIds;
	private String toComponentInstanceIds;
	private String status;
	private Date beginTime;
	private Date endTime;
	private String createUserId;
	private Integer processType;
	private List<ProcessRecord> records=new ArrayList<>();

	public synchronized void addRecord(ProcessRecord record) {
		if (CollectionUtils.isEmpty(records)) {
			records = new ArrayList<>();
		}
		records.add(record);
	}


	public synchronized List<ProcessRecord> getRecords() {
		return records;
	}

	public  void setRecords(List<ProcessRecord> records) {
		this.records = records;
	}

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public  String getFromComponentInstanceIds() {
		return fromComponentInstanceIds;
	}

	public  void setFromComponentInstanceIds(String fromComponentInstanceIds) {
		this.fromComponentInstanceIds = fromComponentInstanceIds;
	}

	public  String getToComponentInstanceIds() {
		return toComponentInstanceIds;
	}

	public  void setToComponentInstanceIds(String toComponentInstanceIds) {
		this.toComponentInstanceIds = toComponentInstanceIds;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public synchronized Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) throws IOException {
		this.endTime = endTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public synchronized String getStatus() {
		return status;
	}

	public synchronized void setStatus(String status) throws IOException {
		this.status = status;
	}

}
