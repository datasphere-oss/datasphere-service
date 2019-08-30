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

package com.datasphere.server.domain.mdm.catalog;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Transactional(readOnly = true)
public class CatalogService {

  @Autowired
  CatalogService catalogService;

  @Autowired
  CatalogTreeService catalogTreeService;

  @Autowired
  CatalogRepository catalogRepository;

  public List<Catalog> findAllCatalogs(String nameContains, String searchDateBy, DateTime from, DateTime to) {
    return Lists.newArrayList(
        catalogRepository.findAll(CatalogPredicate.searchList(nameContains, searchDateBy, from, to)));
  }

  public List<Catalog> findOnlySubCatalogs(String catalogId, String nameContains, String searchDateBy, DateTime from, DateTime to) {
    if (catalogId == null || Catalog.ROOT.equals(catalogId)) {
      return catalogRepository.findRootSubCatalogs(nameContains, searchDateBy, from, to);
    } else {
      return catalogRepository.findSubCatalogs(catalogId, nameContains, searchDateBy, from, to, true);
    }
  }

  public List<Catalog> findAllSubCatalogs(String catalogId, String nameContains, String searchDateBy, DateTime from, DateTime to) {
    return catalogRepository.findSubCatalogs(catalogId, nameContains, searchDateBy, from, to, false);
  }

  /**
   * TreeView Projection 을 위한 처리
   */
  public List<Map<String, Object>> findSubCatalogsForTreeView(String catalogId) {

    List<Catalog> catalogs = findOnlySubCatalogs(catalogId, null, null, null, null);

    return catalogs.stream()
                   .map(catalog -> catalog.getTreeView(this))
                   .collect(Collectors.toList());

  }

  public boolean existSubCatalogs(String catalogId) {
    return countSubCatalogs(catalogId) > 0 ? true : false;
  }

  public Long countSubCatalogs(String catalogId) {
    return catalogRepository.countOnlySubCatalogs(catalogId);
  }

  public List<Map<String, String>> findHierarchies(String catalogId) {

    List<Catalog> books = catalogRepository.findAllAncestors(catalogId);

    return books.stream()
                .map(catalog -> {
                  Map<String, String> map = Maps.newLinkedHashMap();
                  map.put("id", catalog.getId());
                  map.put("name", catalog.getName());
                  return map;
                })
                .collect(Collectors.toList());
  }

  @Transactional
  public Catalog copy(Catalog sourceCatalog, Optional<String> toCatalogId) {

    if (!toCatalogId.isPresent() && catalogRepository.findById(toCatalogId.get()) == null) {
      throw new IllegalArgumentException("Not found catalog to copy : " + toCatalogId);
    }

    // toCatalogId 가 존재하지 않을 경우 같은 Catalog 에 복사하고,
    // Catalog 명이 "Copy of" Prefix를 붙여줍니다.
    Catalog copiedCatalog = sourceCatalog.copyOf(toCatalogId);

    catalogRepository.saveAndFlush(copiedCatalog);
    catalogTreeService.createTree(copiedCatalog);

    return copiedCatalog;

  }

  @Transactional
  public Catalog move(Catalog moveCatalog, Optional<String> toCatalogId) {

    if (!toCatalogId.isPresent()
        && !Catalog.ROOT.equals(toCatalogId.get())
        && catalogRepository.findById(toCatalogId.get()) == null) {
      throw new IllegalArgumentException("Not found catalog to move : " + toCatalogId);
    }

    if (!toCatalogId.isPresent() || Catalog.ROOT.equals(toCatalogId.get())) {
      moveCatalog.setParentId(null);
    } else {
      moveCatalog.setParentId(toCatalogId.get());
    }

    catalogTreeService.editTree(moveCatalog);

    return catalogRepository.save(moveCatalog);
  }

}
