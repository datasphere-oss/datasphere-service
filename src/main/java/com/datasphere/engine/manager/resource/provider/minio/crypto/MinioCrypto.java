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

public class MinioCrypto {

    public final static int SALT_SIZE = 32;

    /*
     * Factories
     */

    public static MinioDecrypter getDecrypter(String secretKey, byte[] raw) throws MinioCryptoException {
        return new MinioDecrypter(secretKey, raw, SALT_SIZE);
    }

    public static MinioCrypter getCrypter(String secretKey, String plainText) throws MinioCryptoException {
        // support only v2 + AES
        return new MinioCrypter(secretKey, plainText, SALT_SIZE, 2, (byte) 0);
    }

}
