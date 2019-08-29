/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.mdm.preview;

import static com.datasphere.server.datasource.Field.COLUMN_NAME_CURRENT_DATETIME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.common.data.ColumnDescription;
import com.datasphere.common.data.ColumnHistogram;
import com.datasphere.common.data.DataGrid;
import com.datasphere.common.data.DataHistogram;
import com.datasphere.common.data.Row;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.datasource.DataType;
import com.datasphere.server.datasource.Field;
import com.datasphere.server.domain.mdm.Metadata;
import com.datasphere.server.domain.mdm.MetadataColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The type Metadata data preview.
 */
public abstract class MetadataDataPreview implements DataGrid, DataHistogram {

  private static Logger LOGGER = LoggerFactory.getLogger(MetadataDataPreview.class);

  /**
   * The Column names.
   */
  //Columns
  List<String> columnNames = Lists.newArrayList();
  /**
   * The Column descriptions.
   */
  List<ColumnDescription> columnDescriptions = Lists.newArrayList();
  /**
   * The Column index.
   */
  Map<String, Integer> columnIndex = Maps.newLinkedHashMap();

  /**
   * The Rows.
   */
  //Rows
  List<Row> rows = Lists.newArrayList();

  /**
   * The Histograms.
   */
  //Histograms
  List<ColumnHistogram> histograms = Lists.newArrayList();

  /**
   * The Limit.
   */
  Integer limit;

  /**
   * The Metadata.
   */
  @JsonIgnore
  Metadata metadata;

  @Override
  public Integer getColumnCount() {
    return columnNames.size();
  }

  @Override
  public List<String> getColumnNames() {
    return columnNames;
  }

  @Override
  public List<ColumnDescription> getColumnDescriptions() {
    return columnDescriptions;
  }

  @Override
  public Map<String, Integer> getColumnIndex() {
    return columnIndex;
  }

  @Override
  public List<Row> getRows() {
    return rows;
  }

  @Override
  public Integer getRowCount() {
    return rows.size();
  }

  @Override
  public List<ColumnHistogram> getHistograms() {
    return histograms;
  }

  /**
   * Instantiates a new Metadata data preview.
   *
   * @param metadata the metadata
   */
  public MetadataDataPreview(Metadata metadata){
    this.metadata = metadata;
  }

  /**
   * Gets limit.
   *
   * @return the limit
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * Sets limit.
   *
   * @param limit the limit
   */
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  /**
   * Get data.
   */
  public void getData(){
    generateColumns(metadata);
    getDataGrid(metadata);
  }

  /**
   * Generate columns.
   *
   * @param metadata the metadata
   */
  protected void generateColumns(Metadata metadata){
    for(MetadataColumn metadataColumn : metadata.getColumns()){
      //skip current_datetime
      if(metadata.getSourceType() == Metadata.SourceType.ENGINE
          && COLUMN_NAME_CURRENT_DATETIME.equals(metadataColumn.getPhysicalName())
          && metadataColumn.getRole() == Field.FieldRole.TIMESTAMP){
        continue;
      }

      columnNames.add(metadataColumn.getPhysicalName());

      //Create column description

      //column field role
      Map additionalMap = metadataColumn.getAdditionalContextMap();
      if(additionalMap == null){
        additionalMap = new HashMap();
      }
      additionalMap.put("role", metadataColumn.getRole());
      ColumnDescription columnDescription = new ColumnDescription(metadataColumn.getName(),
                                                                  metadataColumn.getType() == null
                                                                      ? DataType.UNKNOWN.toLogicalType().toString()
                                                                      : metadataColumn.getType().toString(),
                                                                  metadataColumn.getPhysicalName(),
                                                                  metadataColumn.getPhysicalType(),
                                                                  GlobalObjectMapper.readValue(metadataColumn.getFormat()),
                                                                  additionalMap);
      columnDescriptions.add(columnDescription);

      columnIndex.put(metadataColumn.getPhysicalName(), metadata.getColumns().indexOf(metadataColumn));
    }
  }

  /**
   * Gets data grid.
   *
   * @param metadata the metadata
   */
  abstract protected void getDataGrid(Metadata metadata);
}
