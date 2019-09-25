package com.datasphere.common.connections.dao;

import com.datasphere.datasource.connections.model.DataSetInstance;

public interface DataSetInstanceDao {
    void insert(DataSetInstance dataSetInstance);
    DataSetInstance get(String id);
    DataSetInstance getDatasetInstanceById(String id);
    void update(DataSetInstance dataSetInstance);
    boolean delete(String id);
}
