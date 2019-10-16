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

package com.datasphere.server.domain.workbench;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by aladin on 2019. 1. 26..
 */
@RepositoryRestResource(path = "workbenchs", itemResourceRel = "workbench"
				, collectionResourceRel = "workbenchs", excerptProjection = WorkbenchProjections.DefaultProjection.class)
public interface WorkbenchRepository extends JpaRepository<Workbench, String> {

//	@Override
//	@RestResource(exported = false)
//	List<Workbench> findAll();
//
//	@Override
//	@RestResource(exported = false)
//	Page<Workbench> findAll(Pageable pageable);
	// 通过名称找到工作表
	@RestResource(exported = false)
	Page<Workbench> findByWorkspaceIdAndNameIgnoreCaseContaining(String workspaceId, String name, Pageable pageable);
}
