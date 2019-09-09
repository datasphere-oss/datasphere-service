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

package com.datasphere.engine.shaker.processor.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.datasphere.engine.shaker.processor.model.ProcessInstance;

public interface ProcessInstanceDao {

	void add(ProcessInstance processInstance);
	void modify(ProcessInstance processInstance);

	@ResultMap("baseMap")
	@Select("select * from process_instance where panel_id = #{0} order by create_time desc limit 1")
	ProcessInstance getLastByPanelId(String panelId);

	int deleteByPanelIdList(List<String> panelIdList);

}
