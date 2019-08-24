package com.datasphere.engine.shaker.processor.prep.dao;

import javax.inject.Singleton;

import com.datasphere.engine.shaker.processor.prep.domain.ProgramOperate;

import java.util.List;

@Singleton
public interface ProgramOperateDao {//extends AbstractDao<ProgramOperate>

    int deleteByProgramId(Integer programId);

    List<ProgramOperate> selectByProgramId(Integer programId);
}
