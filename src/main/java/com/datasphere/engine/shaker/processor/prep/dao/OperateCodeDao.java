package com.datasphere.engine.shaker.processor.prep.dao;

import javax.inject.Singleton;

import com.datasphere.engine.shaker.processor.prep.domain.OperateCode;

import java.util.List;

public interface OperateCodeDao {   // extends AbstractDao<OperateCode>
    List<OperateCode> selectAll();
}
