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

import static com.datasphere.server.domain.mdm.MetadataErrorCodes.LINEAGE_COLUMN_MISSING;
import static com.datasphere.server.domain.mdm.MetadataErrorCodes.LINEAGE_DATASET_ERROR;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.FR_COL_NAME;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.FR_META_ID;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.FR_META_NAME;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.TO_COL_NAME;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.TO_META_ID;
import static com.datasphere.server.domain.mdm.lineage.LineageNode.TO_META_NAME;

import com.datasphere.server.domain.dataprep.entity.PrDataset;
import com.datasphere.server.domain.dataprep.entity.PrDataset.DS_TYPE;
import com.datasphere.server.domain.dataprep.exceptions.PrepErrorCodes;
import com.datasphere.server.domain.dataprep.exceptions.PrepException;
import com.datasphere.server.domain.dataprep.exceptions.PrepMessageKey;
import com.datasphere.server.domain.dataprep.repository.PrDatasetRepository;
import com.datasphere.server.domain.dataprep.teddy.DataFrame;
import com.datasphere.server.domain.dataprep.teddy.Row;
import com.datasphere.server.domain.dataprep.teddy.exceptions.CannotSerializeIntoJsonException;
import com.datasphere.server.domain.dataprep.transform.PrepTransformService;
import com.datasphere.server.domain.mdm.Metadata;
import com.datasphere.server.domain.mdm.MetadataRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineageEdgeService {

  private static Logger LOGGER = LoggerFactory.getLogger(LineageEdgeService.class);

  @Autowired
  LineageEdgeRepository edgeRepository;

  @Autowired
  MetadataRepository metadataRepository;

  @Autowired
  PrDatasetRepository datasetRepository;

  @Autowired
  PrepTransformService prepTransformService;

  public LineageEdgeService() {
  }

  @Transactional(rollbackFor = Exception.class)
  public LineageEdge createEdge(String frMetaId, String toMetaId,
      String frMetaName, String toMetaName, String frColName, String toColName,
      Long tier, String desc)
      throws Exception {
    LOGGER.trace("createEdge(): start");

    LineageEdge lineageEdge = new LineageEdge(frMetaId, toMetaId, frMetaName, toMetaName, frColName, toColName, tier, desc);
    edgeRepository.saveAndFlush(lineageEdge);

    LOGGER.trace("createEdge(): end");
    return lineageEdge;
  }

  public List<LineageEdge> listEdge() {
    LOGGER.trace("listEdge(): start");

    List<LineageEdge> edges = edgeRepository.findAll();

    LOGGER.trace("listEdge(): end");
    return edges;
  }

  public LineageEdge getEdge(String edgeId) {
    LOGGER.trace("getEdge(): start");

    LineageEdge lineageEdge = edgeRepository.findById(edgeId).get();

    LOGGER.trace("getEdge(): end");
    return lineageEdge;
  }

  public List<LineageEdge> loadLineageMapDsByDsName(String wrangledDsName) {
    if (wrangledDsName == null) {
      wrangledDsName = "DEFAULT_LINEAGE_MAP";
    }

    List<PrDataset> datasets = datasetRepository.findByDsName(wrangledDsName);
    if (datasets.size() == 0) {
      LOGGER.error("loadLineageMapDsByDsName(): Cannot find W.DS by name: " + wrangledDsName);
      throw PrepException
          .create(PrepErrorCodes.PREP_TRANSFORM_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATASET);
    }

    // Use the 1st one if many.
    for (PrDataset dataset : datasets) {
      if (dataset.getDsType() == DS_TYPE.WRANGLED) {
        return loadLineageMapDs(dataset.getDsId(), dataset.getDsName());
      }
    }

    assert false;
    return new ArrayList();
  }

  private boolean isSame(String a, String b) {
    return a == null ? false : a.equals(b);
  }

  private LineageEdge findOrNew(String frMetaId, String toMetaId, Long tier, String desc) {
    Metadata frMeta = metadataRepository.findById(frMetaId).get();
    Metadata toMeta = metadataRepository.findById(toMetaId).get();
    String frMetaName = frMeta.getName();
    String toMetaName = toMeta.getName();

    LineageEdge newEdge = new LineageEdge(frMetaId, toMetaId, frMetaName, toMetaName, tier, desc);

    for (LineageEdge edge : edgeRepository.findAll()) {
      if (isSame(edge.getFrMetaId(), frMetaId) &&
          isSame(edge.getToMetaId(), toMetaId) &&
          isSame(edge.getDesc(), desc)) {
        return edge;
      }
    }

    return newEdge;
  }

  /**
   * Read a dependency dataset. (feat. Dataprep)
   *
   * For metadata dependency we use 7 Columns below:
   *
   * - fr_meta_name, to_meta_name
   * - fr_col_name, to_col_name
   * - desc
   * - (fr_meta_id, to_meta_id)
   *
   * If fr_col_name and to_col_name are not nulls, then it's the dependency between columns.
   * If they're nulls, then it's between metadata.
   * We find the metadata by meta names:
   * - If cannot find any, then throw an exception.
   * - If found many, use one of them. Rule is not set. So in this case, you should provide meta_id.
   *
   * Belows are examples. (meta_ids are optional and out of consideration here)
   *
   * +---------------------+-------------------+---------------+-------------+--------------------+
   * | fr_meta_name        | fr_col_name       | to_meta_name  | to_col_name | desc               |
   * +---------------------+-------------------+---------------+-------------+--------------------+
   * | Imported dataset #1 |                   | Hive table #1 |             | Cleansing #1       |
   * | Hive table #2       |                   | Hive table #1 |             | UPDATE SQL #1      |
   * | Hive table #1       |                   | Datasource #1 |             | Batch ingestion #1 |
   * +---------------------+-------------------+---------------+-------------+--------------------+
   *
   * +---------------------+-------------------+---------------+-------------+--------------------+
   * | fr_meta_name        | fr_col_name       | to_meta_name  | to_col_name | desc               |
   * +---------------------+-------------------+---------------+-------------+--------------------+
   * | Imported dataset #1 | col_1             | Hive table #1 | col_1       | Cleansing #1       |
   * | Imported dataset #1 | col_2             | Hive table #1 | col_2       | Cleansing #1       |
   * | Hive table #2       | rebate            | Hive table #1 | col_2       | UPDATE SQL #1      |
   * | Hive table #1       | col_1             | Datasource #1 | region_name | Batch ingestion #1 |
   * | Hive table #1       | col_2             | Datasource #1 | region_sum  | Batch ingestion #1 |
   * +---------------------+-------------------+---------------+-------------+--------------------+
   */
  public List<LineageEdge> loadLineageMapDs(String wrangledDsId, String wrangledDsName) {
    DataFrame df = null;

    try {
      df = prepTransformService.loadWrangledDataset(wrangledDsId);
    } catch (IOException e) {
      String msg = "IOException occurred: dsName=" + wrangledDsName;
      LOGGER.error(msg);
      throw new LineageException(LINEAGE_DATASET_ERROR, msg);
    } catch (CannotSerializeIntoJsonException e) {
      String msg = "CannotSerializeIntoJsonException occurred: dsName=" + wrangledDsName;
      LOGGER.error(msg);
      throw new LineageException(LINEAGE_DATASET_ERROR, msg);
    }

    List<LineageEdge> newEdges = new ArrayList();

    // NOTE:
    // BATCH UPLOADING WILL DELETE ALL EXISTING EDGES.
    // The first reason is that currently, we don't have any way to delete a wrong edge. (UI)
    // The second reason is that I guess users might use this feature to reset all dependencies.
    edgeRepository.deleteAll();

    for (Row row : df.rows) {
      if (getValue(row, FR_COL_NAME, false) != null) {
        // TODO: Column dependency rows are ignored for now.
        continue;
      }

      String frMetaId = getMetaIdByRow(row, true);
      String toMetaId = getMetaIdByRow(row, false);
      String frMetaName = getValue(row, FR_META_NAME, true);
      String toMetaName = getValue(row, TO_META_NAME, true);
      String frColName = getValue(row, FR_COL_NAME, false);
      String toColName = getValue(row, TO_COL_NAME, false);

      Long tier = null;
      if (df.colNames.contains("tier")) {
        tier = (Long) row.get("tier");
      }
      String desc = (String) row.get("desc");

      // Over write if exists. (UPSERT)
      LineageEdge edge = findOrNew(frMetaId, toMetaId, tier, desc);

      edgeRepository.save(edge);
      newEdges.add(edge);
    }

    return newEdges;
  }

  // Null if the key is contained, or value is an empty string.
  private String getValue(Row row, String colName, boolean mandatory) {
    if (!row.nameIdxs.containsKey(colName)) {
      if (mandatory) {
        String msg = "Cannot find column from lineage map file: " + colName;
        LOGGER.error(msg);
        throw new LineageException(LINEAGE_COLUMN_MISSING, msg);
      }
      return null;
    }

    String col = (String) row.get(colName);
    if (col == null || col.length() == 0) {
      return null;
    }

    return col;
  }

  private String getMetaIdByRow(Row row, boolean upstream) {
    String metaId;
    String metaName;

    if (upstream) {
      metaId = getValue(row, FR_META_ID, false);
      metaName = getValue(row, FR_META_NAME, true);
    } else {
      metaId = getValue(row, TO_META_ID, false);
      metaName = getValue(row, TO_META_NAME, true);
    }

    // When ID is known
    if (metaId != null) {
      return metaId;
    }

    List<Metadata> metadatas = metadataRepository.findByName(metaName);

    if (metadatas.size() == 0) {
      LOGGER.error(String.format("loadLineageMapDs(): Metadata %s not found: ignored", metaName));
      // TODO: Create an empty metadata (place-holder)
      return null;
    }

    // Return the first one. If duplicated, ignore the rest.
    return metadatas.get(0).getId();
  }
}
