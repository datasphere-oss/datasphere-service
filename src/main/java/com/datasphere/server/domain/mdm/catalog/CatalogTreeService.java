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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by aladin on 2019. 12. 21..
 */
@Component
@Transactional(readOnly = true)
public class CatalogTreeService {

  private static Logger LOGGER = LoggerFactory.getLogger(CatalogTreeService.class);

  @Autowired
  CatalogRepository catalogRepository;

  @Autowired
  CatalogTreeRepository catalogTreeRepository;

  public CatalogTreeService() {
  }

  @Transactional
  public void createSelfTree(Catalog catalog) {
    CatalogTree tree = new CatalogTree(catalog.getId(), catalog.getId(), 0);
    catalogTreeRepository.save(tree);
  }

  @Transactional
  public void createTree(Catalog catalog) {
    List<CatalogTree> catalogTrees = Lists.newArrayList();
    catalogTrees.add(new CatalogTree(catalog.getId(), catalog.getId(), 0));

    if (catalog.getParentId() == null) {
      catalogTreeRepository.saveAll(catalogTrees);
      return;
    }

    Catalog parentCatalog = catalogRepository.findById(catalog.getParentId()).get();
    if (parentCatalog == null) {
      throw new IllegalArgumentException("Invalid parent Catalog Id : " + catalog.getParentId());
    }

    List<CatalogTree> ancestors = catalogTreeRepository.findByIdDescendant(parentCatalog.getId());

    for (CatalogTree ancestor : ancestors) {
      catalogTrees.add(new CatalogTree(ancestor.getId().getAncestor(), catalog.getId(), ancestor.getDepth() + 1));
    }

    catalogTreeRepository.saveAll(catalogTrees);
  }

  @Transactional
  public void editTree(Catalog catalog) {

    List<CatalogTree> catalogTrees = Lists.newArrayList();
    List<String> deleteDescendants = Lists.newArrayList();

    if (catalog.getParentId() == null) {

      List<CatalogTree> descendants = catalogTreeRepository.findDescendantNotAncenstor(catalog.getId());
      for (CatalogTree catalogTree : descendants) {
        deleteDescendants.add(catalogTree.getId().getDescendant());
        catalogTrees.add(new CatalogTree(catalog.getId(), catalogTree.getId().getDescendant(), catalogTree.getDepth()));
      }

    } else {

      Catalog parentCatalog = catalogRepository.findById(catalog.getParentId()).get();
      if (parentCatalog == null) {
        throw new IllegalArgumentException("Invalid parent Catalog Id : " + catalog.getParentId());
      }

      List<CatalogTree> ancestors = catalogTreeRepository.findByIdDescendant(parentCatalog.getId());
      Map<String, Integer> depthMap = Maps.newHashMap();
      int depth;
      for (CatalogTree ancestor : ancestors) {
        depth = ancestor.getDepth() + 1;
        catalogTrees.add(new CatalogTree(ancestor.getId().getAncestor(), catalog.getId(), depth));
        depthMap.put(ancestor.getId().getAncestor(), depth);
      }

      List<CatalogTree> descendants = catalogTreeRepository.findDescendantNotAncenstor(catalog.getId());
      for (CatalogTree catalogTree : descendants) {
        deleteDescendants.add(catalogTree.getId().getDescendant());
        depthMap.forEach((ancestor, i) ->
                             catalogTrees.add(new CatalogTree(ancestor, catalogTree.getId().getDescendant(), i + catalogTree.getDepth()))
        );
      }
    }

    catalogTreeRepository.deleteEditedTree(deleteDescendants.isEmpty() ? null : deleteDescendants, catalog.getId());

    catalogTreeRepository.saveAll(catalogTrees);
  }

  @Transactional
  public void deleteTree(Catalog catalog) {
    // Delete sub-catalog.
    List<CatalogTree> descendants = catalogTreeRepository.findDescendantNotAncenstor(catalog.getId());
    if (descendants.size() > 0) {
      for (CatalogTree catalogTree : descendants) {
        String descendantId = catalogTree.getId().getDescendant();
        catalogRepository.deleteById(descendantId);
        catalogTreeRepository.deteleAllTree(descendantId);
      }
    }

    catalogTreeRepository.deteleAllTree(catalog.getId());
  }
}
