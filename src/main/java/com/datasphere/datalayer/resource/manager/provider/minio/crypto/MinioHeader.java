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

package com.datasphere.datalayer.resource.manager.provider.minio.crypto;

public interface MinioHeader {
    public byte getVersion();

    public byte getCipher();

    public int getLength();

    public boolean isFinal();

    public byte[] getNonce();

    public byte[] getAddData();

    public byte[] getRaw();

    public void setCipher(byte b);

    public void setLength(int i);

    public void setFinal(boolean b);

    public void setNonce(byte[] b);

    public void clear();

}