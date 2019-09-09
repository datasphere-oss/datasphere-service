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

