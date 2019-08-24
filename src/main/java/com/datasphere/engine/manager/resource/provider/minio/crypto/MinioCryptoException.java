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

package com.datasphere.engine.manager.resource.provider.minio.crypto;

public class MinioCryptoException extends Exception {

    private static final long serialVersionUID = -7450996938221130042L;

    public MinioCryptoException() {
        super();
    }

    public MinioCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioCryptoException(String message) {
        super(message);
    }

    public MinioCryptoException(Throwable cause) {
        super(cause);
    }
}
