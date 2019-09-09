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

import com.datasphere.engine.shaker.processor.prep.model.Program;

import java.util.List;

public interface ProgramDao {

//    List<Program> list(@Param("processId") String processId);

//    int size(@Param("processId") String processId);

//    int delete(@Param("program_id") String program_id);

//    int create(Program program);
//
//    int updateVersion(@Param("programId") Integer programId, @Param("version") Integer version);
//
//    int rename(@Param("programId") Integer programId, @Param("newName") String newName);

    Program getDefault(@Param("processId") String processId);
//
//    int setDefault(@Param("programId") Integer programId);
//
//    int unsetDefault(@Param("processId") String processId);
//
//    ProgramData selectById(@Param("programId") Integer programId);
//
//    List<ProgramData> selectByProcessId(String id);
//
//    List<ProgramData> selectByEvaluationId(Integer evaluationId);
//
//    int deleteByProgramId(@Param("programId") Integer programId);

    List<Program> get(@Param("programids") String programids);

    Program findById(@Param("id") Integer programId);
}
