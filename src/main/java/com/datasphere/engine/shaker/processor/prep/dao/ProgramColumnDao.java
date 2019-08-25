package com.datasphere.engine.shaker.processor.prep.dao;

import org.apache.ibatis.annotations.Param;

import com.datasphere.engine.shaker.processor.prep.model.ProgramColumn;

import javax.inject.Singleton;
import java.util.List;

public interface ProgramColumnDao {// extends AbstractDao<ProgramColumn>

    /**
     * 给某个方案里的列的order增加incOrder
     * @param programId
     * @param incOrder     增加量
     * @param rule         order大于等于rule的
     * @return
     */
    Integer incOrderByRule(@Param("programId") Integer programId, @Param("incOrder") Integer incOrder, @Param("rule") Integer rule);

    List<ProgramColumn> selectByOrder(@Param("programId") Integer programId);

    int deleteByProgramId(Integer programId);

    int deleteColumns(@Param("programId") Integer programId, @Param("ids") List<Integer> columnIds);
}
