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

package com.datasphere.engine.manager.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datasphere.engine.manager.resource.model.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	Long countByType(String type);

	Long countByProvider(String provider);

	Long countByScopeId(String scopeId);

	Long countByUserIdAndScopeId(String userId, String scopeId);

	Long countByTypeAndScopeId(String type, String scopeId);

	Long countByProviderAndScopeId(String provider, String scopeId);

	List<Resource> findByType(String type);

	List<Resource> findByType(String type, Sort sort);

	Page<Resource> findByType(String type, Pageable pageable);

	List<Resource> findByUserId(String userId);

	List<Resource> findByUserId(String userId, Sort sort);

	Page<Resource> findByUserId(String userId, Pageable pageable);

	List<Resource> findByScopeId(String scopeId);

	List<Resource> findByScopeId(String scopeId, Sort sort);

	Page<Resource> findByScopeId(String scopeId, Pageable pageable);

	List<Resource> findByProvider(String provider);

	List<Resource> findByProvider(String provider, Sort sort);

	Page<Resource> findByProvider(String provider, Pageable pageable);

	List<Resource> findByTypeAndScopeId(String type, String scopeId);

	List<Resource> findByTypeAndScopeId(String type, String scopeId, Sort sort);

	Page<Resource> findByTypeAndScopeId(String type, String scopeId, Pageable pageable);

	List<Resource> findByProviderAndScopeId(String provider, String scopeId);

	List<Resource> findByProviderAndScopeId(String provider, String scopeId, Sort sort);

	Page<Resource> findByProviderAndScopeId(String provider, String scopeId, Pageable pageable);

	List<Resource> findByUserIdAndScopeId(String userId, String scopeId);

}
