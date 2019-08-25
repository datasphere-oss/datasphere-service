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

package com.datasphere.server.connections.jdbc.accessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The interface Jdbc row mapper.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface JdbcRowMapper<T> {
  /**
   * Map row t.
   *
   * @param rs     the rs
   * @param rowNum the row num
   * @return the t
   * @throws SQLException the sql exception
   */
  T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
