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
