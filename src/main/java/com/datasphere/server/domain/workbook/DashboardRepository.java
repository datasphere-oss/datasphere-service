/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.workbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

import com.datasphere.server.datasource.DataSource;

/**
 * Created by aladin on 2019. 1. 26..
 */
@RepositoryRestResource(path = "dashboards", itemResourceRel = "dashboard"
    , collectionResourceRel = "dashboards", excerptProjection = DashboardProjections.DefaultProjection.class)
public interface DashboardRepository extends JpaRepository<DashBoard, String>,
                                    QuerydslPredicateExecutor<DashBoard> {

  Integer countByWorkBook(WorkBook workBook);

  @Query(value = "SELECT db FROM DashBoard db JOIN FETCH db.widgets JOIN FETCH db.dataSources ",
      countQuery = "SELECT count(db) FROM DashBoard db")
  Page<DashBoard> findDashBoardsWithDataSourceAndWidgets(Pageable pageable);

  List<DashBoard> findByWorkBookAndSeqGreaterThan(WorkBook workBook, Integer Seq);

  @Query("SELECT DISTINCT ds FROM DashBoard dashBoard " +
              "LEFT OUTER JOIN dashBoard.workBook wb " +
              "INNER JOIN dashBoard.dataSources ds " +
              "WHERE wb.id = :workbookId ORDER BY ds.name ASC")
  List<DataSource> findAllDataSourceInDashboard(@Param("workbookId") String workbookId);
}
