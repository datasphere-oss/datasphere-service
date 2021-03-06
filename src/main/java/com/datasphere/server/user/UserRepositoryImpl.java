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

package com.datasphere.server.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Created by aladin on 2019. 1. 23..
 */
public class UserRepositoryImpl implements UserSearchRepository {

  @Autowired
  private EntityManager entityManager;

  private Map<String, SortField> sortFieldMap = Maps.newHashMap();

  public UserRepositoryImpl() {
    sortFieldMap.put("fullName", new SortField("sortFullName", SortField.Type.STRING));
    sortFieldMap.put("createdTime", new SortField("createdTime.mils", SortField.Type.STRING));
    sortFieldMap.put("modifiedTime", new SortField("modifiedTime.mils", SortField.Type.STRING));
  }

  @Override
  public Page<User> searchByKeyword(@Param("q") String keywords, Pageable pageable) {

    if(StringUtils.isBlank(keywords)) {
      keywords = "*";
    }

    // Must be retrieved inside a transaction to take part of
    final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    // Prepare a search query builder
    final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();

    // This is a boolean junction... I'll add at least a keyword query
    final BooleanJunction<BooleanJunction> outer = queryBuilder.bool();
    outer.must(
        queryBuilder
            .keyword()
            .wildcard()
            .onFields("username", "fullName")
            .matching(keywords)
            .createQuery()
    );

    FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(outer.createQuery(), User.class);
    fullTextQuery.setFirstResult((int)pageable.getOffset());
    fullTextQuery.setMaxResults(pageable.getPageSize());
    fullTextQuery.setSort(getSearchSort(pageable));

    return new PageImpl<>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
  }

  @Override
  public Page<User> searchByQuery(@Param("q") String query, Pageable pageable) {
    final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    FullTextQuery fullTextQuery;
    try {
      final QueryParser queryParser = new QueryParser("content", fullTextEntityManager.getSearchFactory().getAnalyzer(User.class));
      fullTextQuery = fullTextEntityManager.createFullTextQuery(queryParser.parse(query), User.class);

    } catch (ParseException e) {
      e.printStackTrace();
      throw new RuntimeException("Fail to search query : " + e.getMessage());
    }

    fullTextQuery.setFirstResult((int)pageable.getOffset());
    fullTextQuery.setMaxResults(pageable.getPageSize());
    fullTextQuery.setSort(getSearchSort(pageable));

    return new PageImpl<>(fullTextQuery.getResultList(), pageable, fullTextQuery.getResultSize());
  }

  private Sort getSearchSort(Pageable pageable) {

    if(pageable == null || pageable.getSort() == null) {
      return null;
    }

    List<SortField> sortFields = Lists.newArrayList();
    for(org.springframework.data.domain.Sort.Order sortOrder : pageable.getSort()) {
      sortFields.add(getAvailableSortField(sortOrder.getProperty(), sortOrder.getDirection()));
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
