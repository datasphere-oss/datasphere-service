package com.datasphere.engine.shaker.processor.prep.dao;

import javax.inject.Singleton;

import com.datasphere.engine.shaker.processor.prep.data.OperateData;
import com.datasphere.engine.shaker.processor.prep.model.Operate;

import java.util.List;

@Singleton
public interface OperateDao {

    int check_delete(Operate operate);

    List<OperateData> selectByProcessId(String id);

    /**
     * 根据columnId获取生成该列的操作
     *
     * @param columnId
     * @return
     */
    List<Operate> findOperateByOutputColumnId(Integer columnId);

    List<OperateData> findOperateDataByOutputColumnId(Integer columnId);

    List<Operate> selectByIdS(List<Integer> ids);

    Integer deleteByIdS(List<Integer> ids);

}

