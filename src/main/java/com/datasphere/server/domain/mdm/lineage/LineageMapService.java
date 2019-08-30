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

package com.datasphere.server.domain.mdm.lineage;

import static com.datasphere.server.domain.mdm.MetadataErrorCodes.LINEAGE_NODE_COUNT_DONE;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.server.domain.mdm.Metadata;
import com.datasphere.server.domain.mdm.MetadataRepository;
import com.datasphere.server.domain.mdm.lineage.LineageMap.ALIGNMENT;

@Service
public class LineageMapService {

  private static Logger LOGGER = LoggerFactory.getLogger(LineageMapService.class);

  @Autowired
  LineageEdgeRepository edgeRepository;

  @Autowired
  MetadataRepository metadataRepository;

  public LineageMapService() {
  }

  private List<LineageEdge> getUpstreamEdgesOf(String metaId) {
    return edgeRepository.findByToMetaId(metaId);
  }

  // Returns true if found any downstream
  private List<LineageEdge> getDownstreamEdgesOf(String metaId) {
    return edgeRepository.findByFrMetaId(metaId);
  }

  private boolean addUpstreamOfCol(LineageMap map) {
    List<LineageMapNode> col = map.nodeGrid.get(map.getCurColNo());
    boolean found = false;

    if (map.getAlignment() == ALIGNMENT.LEFT) {   // No upstream is needed.
      return false;
    }

    for (LineageMapNode node : col) {
      String metaId = node.getMetaId();
      List<LineageEdge> edges = getUpstreamEdgesOf(metaId);
      for (LineageEdge edge : edges) {
        String newMetaId = edge.getFrMetaId();
        LineageMapNode newNode = new LineageMapNode(newMetaId, getMetaName(newMetaId));
        if (found == false) {
          map.addColBefore();
          found = true;
        }
        map.addFrNode(newNode, node);
        map.getNeedEdges().add(edge);
      }
    }

    return found;
  }

  // Returns true if found any downstream
  private boolean addDownstreamOfCol(LineageMap map) {
    List<LineageMapNode> col = map.nodeGrid.get(map.getCurColNo());
    boolean found = false;

    if (map.getAlignment() == ALIGNMENT.RIGHT) {   // No downstream is needed.
      return false;
    }

    for (LineageMapNode node : col) {
      String metaId = node.getMetaId();
      List<LineageEdge> edges = getDownstreamEdgesOf(metaId);
      for (LineageEdge edge : edges) {
        String newMetaId = edge.getToMetaId();
        LineageMapNode newNode = new LineageMapNode(newMetaId, getMetaName(newMetaId));
        if (found == false) {
          map.addColAfter();
          found = true;
        }
        map.addToNode(newNode, node);
        map.getNeedEdges().add(edge);
      }
    }

    return found;
  }

  public LineageMap getLineageMap(String metaId, int nodeCnt, ALIGNMENT alignment) {
    LineageMap map = new LineageMap(nodeCnt, alignment);
    boolean found;

    LineageMapNode masterNode = new LineageMapNode(metaId, getMetaName(metaId));

    map.addColBefore();
    map.addFrNode(masterNode, null);

    try {
      do {
        found = addUpstreamOfCol(map);
      } while (found);
    } catch (LineageException e) {
      if (e.getCode() != LINEAGE_NODE_COUNT_DONE) {
        throw e;
      }
    }

    map.setCurColNo(map.getMasterColNo());

    try {
      do {
        found = addDownstreamOfCol(map);
      } while (found);
    } catch (LineageException e) {
      if (e.getCode() != LINEAGE_NODE_COUNT_DONE) {
        throw e;
      }
    }

    // TODO: If a node is a downstream of another node, re-place it next to the upstream.

    // TODO: Sort nodes in each depth. longest-downsteram first

    return map;
  }

  private Long getMinTier(List<LineageEdge> edges) {
    Long min = null;

    for (LineageEdge edge : edges) {
      if (edge.getTier() == null) {
        continue;
      } else if (min == null) {
        min = edge.getTier();
      } else if (edge.getTier() < min) {
        min = edge.getTier();
      }
    }

    return min;
  }

  private Long getMaxTier(List<LineageEdge> edges) {
    Long max = null;

    for (LineageEdge edge : edges) {
      if (edge.getTier() == null) {
        continue;
      } else if (max == null) {
        max = edge.getTier();
      } else if (edge.getTier() > max) {
        max = edge.getTier();
      }
    }

    return max;
  }

  private String getMetaName(String metaId) {
    Optional<Metadata> metadatas = metadataRepository.findById(metaId);
    return metadatas.get().getName();
  }

}
