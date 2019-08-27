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

import java.sql.SQLException;

public class JdbcDataConnectionException extends SQLException{

	JdbcDataConnectionErrorCodes codes;

	public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, String message, Throwable cause) {
		this.codes = codes;
	}

	public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, Throwable cause) {
		this.codes = codes;
	}

	public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, String message) {
		this.codes = codes;
	}

	public JdbcDataConnectionErrorCodes getCode() {
		return this.codes;
	}

}
