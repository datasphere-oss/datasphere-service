package com.datasphere.engine.shaker.processor.prep.dao;

import org.apache.ibatis.annotations.Param;

import com.datasphere.engine.shaker.processor.prep.data.DataProcessData;

public interface DataProcessDao {

    ProcessMetadata selectById(@Param("process_id") String process_id);
}
