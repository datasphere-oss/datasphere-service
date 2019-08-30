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

package com.datasphere.server.domain.mdm;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by aladin on 2019. 8. 30..
 */
@RepositoryRestResource(path = "metadatas", itemResourceRel = "metadata", collectionResourceRel = "metadatas",
    excerptProjection = MetadataProjections.DefaultProjection.class)
public interface MetadataRepository extends JpaRepository<Metadata, String>,
    QuerydslPredicateExecutor<Metadata>{//, MetadataRepositoryExtends {

	Page<Metadata> searchMetadatas(Metadata.SourceType sourceType, String catalogId, String tag, String nameContains,
            String searchDateBy, DateTime from, DateTime to, Pageable pageable);

	List<Metadata> findBySource(String sourceId, String schema, List<String> table);
	
	List<Metadata> findBySource(List<String> sourceIds);
	
	List<Metadata> findByName(String name);
	
	
	List<MetadataStatsDto> countBySourceType();
	
}
