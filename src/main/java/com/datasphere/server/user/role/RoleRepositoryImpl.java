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

package com.datasphere.server.user.role;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.querydsl.jpa.JPQLQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Created by aladin on 2019. 1. 23..
 */
public class RoleRepositoryImpl extends QuerydslRepositorySupport implements RoleRepositoryExtends, RoleSearchRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);

  @Autowired
  private EntityManager entityManager;

  private Map<String, SortField> sortFieldMap = Maps.newHashMap();

  public RoleRepositoryImpl() {
    super(Role.class);

    sortFieldMap.put("name", new SortField("name.sort", SortField.Type.STRING));
    sortFieldMap.put("createdTime", new SortField("createdTime.mils", SortField.Type.STRING));
    sortFieldMap.put("modifiedTime", new SortField("modifiedTime.mils", SortField.Type.STRING));
  }

  /**
   *
   * @param scope
   * @param includePermissions
   * @return
   */
  @Override
  public List<String> findRoleNamesByScopeAndPerm(Role.RoleScope scope,
                                                String... includePermissions) {

    return findRoleNamesByScopeAndPerm(scope, Lists.newArrayList(), includePermissions);
  }

  @Override
  public List<String> findRoleNamesByScopeAndPerm(Role.RoleScope scope, List<RoleSet> roleSets,
                                                  String... includePermissions) {

    Preconditions.checkNotNull(scope, "scope required.");

    QRole qRole = QRole.role;
    JPQLQuery query = from(qRole).select(qRole.name);

    query.where(qRole.scope.eq(scope));

    if(CollectionUtils.isNotEmpty(roleSets)) {
      query.where(qRole.roleSet.in(roleSets));
    }

    if(includePermissions != null && includePermissions.length > 0) {
      query.where(qRole.permissions.any().name.in(includePermissions));
    }

    return query.fetch();
  }

  @Override
  public List<Role> findRoleByDirectoryId(String... directoryIds) {
    QRole qRole = QRole.role;
    JPQLQuery query = from(qRole).distinct()
        .join(qRole.permissions).fetchJoin()
        .join(qRole.directories).where(qRole.directories.any().directoryId.in(directoryIds));

    return query.fetch();
  }

  @Override
  public List<String> findRoleNameByDirectoryId(String... directoryIds) {
    QRole qRole = QRole.role;
    JPQLQuery query = from(qRole).distinct().select(qRole.name)
                                 .where(qRole.directories.any().directoryId.in(directoryIds));

    return query.fetch();
  }

  @Override
  public Page<Role> searchByKeyword(String keywords, Pageable pageable) {
    // Must be retrieved inside a transaction to take part of
    final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    // Prepare a search query builder
    final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Role.class).get();

    // This is a boolean junction... I'll add at least a keyword query
    final BooleanJunction<BooleanJunction> outer = queryBuilder.bool();
    outer.must(
        queryBuilder
            .keyword()
            .wildcard()
            .onFields("name")
            .matching(keywords)
            .createQuery()
    );

    FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(outer.createQuery(), Role.class);
    fullTextQuery.setFirstResult((int)pageable.getOffset());
    fullTextQuery.setMaxResults(pageable.getPageSize());
    fullTextQuery.setSort(getSearchSort(pageable));

    return new PageImpl<>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
  }

  @Override
  public Page<Role> searchByQuery(String query, Pageable pageable) {
    final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    FullTextQuery fullTextQuery;
    try {

      final QueryParser queryParser = new QueryParser("name", fullTextEntityManager.getSearchFactory().getAnalyzer(Role.class));
      Query parsedQuery = queryParser.parse(query);
      LOGGER.debug("{}, Parsed {}", query, parsedQuery);

      fullTextQuery = fullTextEntityManager.createFullTextQuery(parsedQuery, Role.class);

    } catch (ParseException e) {
      e.printStackTrace();
      throw new RuntimeException("Fail to search query : " + e.getMessage());
    }

    fullTextQuery.setFirstResult((int)pageable.getOffset());
    fullTextQuery.setMaxResults(pageable.getPageSize());

    Sort sort = getSearchSort(pageable);

    LOGGER.debug("Sort : {}", Arrays.toString(sort.getSort()));

    if(sort.getSort().length > 0) {
      fullTextQuery.setSort(getSearchSort(pageable));
    }

    return new PageImpl<>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
  }

  private Sort getSearchSort(Pageable pageable) {

    List<SortField> sortFields = Lists.newArrayList();

    if(pageable != null && pageable.getSort() != null) {
      for (org.springframework.data.domain.Sort.Order sortOrder : pageable.getSort()) {
        sortFields.add(getAvailableSortField(sortOrder.getProperty(), sortOrder.getDirection()));
      }
    }

    return new Sort(sortFields.toArray(new SortField[0]));
  }

  private SortField getAvailableSortField(String propertyName, org.springframework.data.domain.Sort.Direction direction) {

    if(sortFieldMap.containsKey(propertyName)) {
      SortField availableSortField = sortFieldMap.get(propertyName);

      boolean reverse = direction == org.springframework.data.domain.Sort.Direction.DESC ? true : false;
      if(availableSortField.getType() != null) {
        return new SortField(availableSortField.getField(), availableSortField.getType(), reverse);
      } else {
        return new SortField(availableSortField.getField(), availableSortField.getComparatorSource(), reverse);
      }

    } else {
      return sortFieldMap.get("name");
    }
  }


}
