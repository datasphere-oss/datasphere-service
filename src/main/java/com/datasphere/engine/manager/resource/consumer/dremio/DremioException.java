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

package com.datasphere.engine.manager.resource.consumer.dremio;

public class DremioException extends Exception {

    private static final long serialVersionUID = -8914131731393882634L;

    public DremioException() {
        super();
    }

    public DremioException(String message, Throwable cause) {
        super(message, cause);
    }

    public DremioException(String message) {
        super(message);
    }

    public DremioException(Throwable cause) {
        super(cause);
    }
}
