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

package com.datasphere.datalayer.resource.manager.common;

public class NoSuchProviderException extends Exception {

	private static final long serialVersionUID = 5159102079326531210L;

	public NoSuchProviderException() {
		super();
	}

	public NoSuchProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchProviderException(String message) {
		super(message);
	}

	public NoSuchProviderException(Throwable cause) {
		super(cause);
	}
}