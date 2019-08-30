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

package com.datasphere.server.datasource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by aladin on 2019. 8. 30..
 */
@RepositoryRestResource(exported = false, excerptProjection = DataSourceAliasProjections.DefaultProjection.class)
public interface DataSourceAliasRepository extends JpaRepository<DataSourceAlias, Long>,
    QuerydslPredicateExecutor<DataSourceAlias> {

  List<DataSourceAlias> findByDashBoardId(String dashboardId);

  DataSourceAlias findDistinctByDataSourceIdAndDashBoardIdAndFieldName(String dataSourceId, String dashboardId, String fieldName);
}
