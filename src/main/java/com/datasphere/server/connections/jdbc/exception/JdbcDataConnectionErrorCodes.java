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

package com.datasphere.server.connections.jdbc.exception;

public enum JdbcDataConnectionErrorCodes {

  GENERAL_ERROR_CODE("JDC0001")
  , HIVE_METASTORE_ERROR_CODE("JDC0002")
  , INVALID_QUERY_ERROR_CODE("JDC0003")
  , CSV_IO_ERROR_CODE("JDC0004")
  , WEBSOCKET_NOT_FOUND_ERROR_CODE("JDC0005")
  , PARTITION_NOT_EXISTED("JDC0006")
  , NO_SUITABLE_DRIVER("JDC0007")
  , DATASOURCE_CONNECTION_ERROR("JDC0008")
  , NOT_FOUND_SUITABLE_DIALECT("JDC0009")
  , NOT_FOUND_SUITABLE_DATA_ACCESSOR("JDC0010")
  , PREVIEW_TABLE_SQL_ERROR("error.server.connections.jdbc.preview.table")
  , STAGEDB_PREVIEW_TABLE_SQL_ERROR("error.server.connections.stagedb.preview.table")
  ;

  String errorCode;

  JdbcDataConnectionErrorCodes(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getCode() {
    return errorCode;
  }
}
