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

public interface MinioPackage {

    public byte version();

    public MinioHeader header();

    public byte[] payload();

    public byte[] cipherText();

    public byte[] nonce();

    public byte[] addData();

    public int length();

    public void clear();

    public void setCipherText(byte[] bytes);

    public byte[] build() throws MinioCryptoException;

}