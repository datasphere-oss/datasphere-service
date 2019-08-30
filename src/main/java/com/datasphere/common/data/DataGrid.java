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

package com.datasphere.common.data;

import java.util.List;
import java.util.Map;

/**
 * The interface Data grid.
 */
public interface DataGrid {
  /**
   * Gets column count.
   *
   * @return the column count
   */
  //Columns
  Integer getColumnCount();

  /**
   * Gets column names.
   *
   * @return the column names
   */
  List<String> getColumnNames();

  /**
   * Gets column descriptions.
   *
   * @return the column descriptions
   */
  List<ColumnDescription> getColumnDescriptions();

  /**
   * Gets column index.
   *
   * @return the column index
   */
  Map<String, Integer> getColumnIndex();

  /**
   * Gets row count.
   *
   * @return the row count
   */
  //Rows
  Integer getRowCount();

  /**
   * Gets rows.
   *
   * @return the rows
   */
  List<Row> getRows();
}
