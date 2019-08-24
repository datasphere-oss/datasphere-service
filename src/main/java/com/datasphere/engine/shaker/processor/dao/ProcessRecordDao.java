package com.datasphere.engine.shaker.processor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.datasphere.engine.shaker.processor.model.ProcessRecord;


public interface ProcessRecordDao {

	void add(ProcessRecord processRecord);

	ProcessRecord get(String id);

	void modify(ProcessRecord processRecord);
	
//	@ResultMap("baseMap")
	@Select("select * from public.process_record where process_id = #{0}")
	List<ProcessRecord> listByProcessInstanceId(String processInstanceId);

	@ResultMap("baseMap")
	@Select("select * from (select * from public.process_record where component_instance_id = #{componentInstanceId} order by create_time desc limit 10) a order by a.create_time asc")
	List<ProcessRecord> getLatestRecord(@Param("componentInstanceId") String componentInstanceId);

	int deleteByPanelIdList(List<String> panelIdList);
}
