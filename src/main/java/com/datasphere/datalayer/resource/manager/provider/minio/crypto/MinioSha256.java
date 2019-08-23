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

import java.nio.charset.Charset;
import java.security.MessageDigest;

import uk.co.lucasweb.aws.v4.signer.SigningException;
import uk.co.lucasweb.aws.v4.signer.functional.Throwables;

public class MinioSha256 {

    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    public static String get(byte[] bytes) {
        return Throwables.returnableInstance(() -> {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            md.getDigestLength();
            return bytesToHex(md.digest());
        }, SigningException::new);
    }

    public static String get(String value, Charset charset) {
        return Throwables.returnableInstance(() -> {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = value.getBytes(charset);
            md.update(bytes);
            md.getDigestLength();
            return bytesToHex(md.digest());
        }, SigningException::new);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            sb.append(hexDigits[(b >> 4) & 0xf]).append(hexDigits[b & 0xf]);
        }
        return sb.toString();
    }
}
