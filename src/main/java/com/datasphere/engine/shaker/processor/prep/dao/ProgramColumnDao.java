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
