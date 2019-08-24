package com.datasphere.server.connections.model;

import com.datasphere.common.data.Dataset;


public class DatasetWrapper extends Dataset {
	
	Pager pager;

	public Pager getPager() {
		return pager;
	}

	public DatasetWrapper setPager(Pager pager) {
		this.pager = pager;
		return this;
	}
	
	public static DatasetWrapper from(Dataset dataset) {
		DatasetWrapper res = new DatasetWrapper();
		res.setDataKey(dataset.getDataKey());
		res.setMessage(dataset.getMessage());
		res.setData(dataset.getData());
		res.setColumnsMeta(dataset.getColumnsMeta());
		res.setCreateTime(dataset.getCreateTime());
		res.setLastModified(dataset.getLastModified());
		return res;
	}
}
