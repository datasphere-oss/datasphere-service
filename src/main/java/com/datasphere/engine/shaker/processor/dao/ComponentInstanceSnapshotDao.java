package com.datasphere.engine.shaker.processor.dao;

import com.datasphere.engine.shaker.processor.model.ComponentInstanceSnapshot;

public interface ComponentInstanceSnapshotDao {

//	@Insert("insert into public.component_instance_snapshot(processId, content) values(#{processId}, #{content})")
	void add(ComponentInstanceSnapshot componentInstanceSnapshot);
}
