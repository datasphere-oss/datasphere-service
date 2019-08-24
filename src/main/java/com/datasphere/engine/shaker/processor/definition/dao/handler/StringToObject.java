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

package com.datasphere.engine.shaker.processor.definition.dao.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.datasphere.engine.core.utils.ObjectMapperUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringToObject extends BaseTypeHandler<Object> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, (String) parameter);
	}

	/**
	 * 从数据库中读取转化成JavaBean的属性会调用这个方法
	 */
	@Override
	public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if(value != null && !value.equals("")) {
			if(value.startsWith("{")) {
				return ObjectMapperUtils.readValue(value, Map.class);
			} else {
				return ObjectMapperUtils.readValue(value, List.class);
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String value = rs.getString(columnIndex);
		if(value != null) {
			if(value.startsWith("{")) {
				return ObjectMapperUtils.readValue(value, Map.class);
			} else {
				return ObjectMapperUtils.readValue(value, List.class);
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String value = cs.getString(columnIndex);
		if(value != null) {
			if(value.startsWith("{")) {
				return ObjectMapperUtils.readValue(value, Map.class);
			} else {
				return ObjectMapperUtils.readValue(value, List.class);
			}
		} else {
			return Collections.emptyList();
		}
	}
}
