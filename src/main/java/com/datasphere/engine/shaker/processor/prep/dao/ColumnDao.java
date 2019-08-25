package com.datasphere.engine.shaker.processor.prep.dao;

import org.apache.ibatis.annotations.Param;

import com.datasphere.engine.shaker.processor.prep.data.ColumnData;
import com.datasphere.engine.shaker.processor.prep.model.Column;

import java.util.List;

public interface ColumnDao {

    List<ColumnData> selectByProcessId(@Param("process_id") String process_id);

//    List<Column> findAllColumByFlag(@Param("process_id") String process_id, @Param("sourceFlag") String sourceFlag);

    List<Column> selectByIdS(@Param("ids") List<Integer> ids);

//    List<Column> findInputColumnsByOperateId(Integer operateId);
//    List<Column> findOutputColumnsByOperateId(Integer operateId);
//    List<Column> selectByColumnId(@Param("id") Integer id);
//    Integer deleteByIdS(@Param("ids") List<Integer> ids);
//    List<Column> selectByProgramId(Integer programId);
}
