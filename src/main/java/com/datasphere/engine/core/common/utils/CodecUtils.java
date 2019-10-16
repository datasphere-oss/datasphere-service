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

package com.datasphere.engine.core.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodecUtils
{
    public static String md5(final String source) throws NoSuchAlgorithmException {
        final byte[] bytes = source.getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes, 0, bytes.length);
        return byteArrayToHex(md.digest());
    }
    
    public static String byteArrayToHex(final byte[] byteArray) {
        final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        final char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (final byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xF];
            resultCharArray[index++] = hexDigits[b & 0xF];
        }
        return new String(resultCharArray);
    }
}
