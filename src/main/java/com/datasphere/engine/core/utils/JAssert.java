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

package com.datasphere.engine.core.utils;


import com.datasphere.server.common.exception.JIllegalOperationException;

public class JAssert
{
    public static void isTrue(final Boolean b) {
        isTrue(b, "Illegal argument!");
    }
    
    public static void isTrue(final Boolean b, final String msg) {
        if (b == null || !b) {
            throw new JIllegalOperationException(msg);
        }
    }

    public static void isTrue(Boolean b,Integer code, String msg) {
        if(b == null || b == false) {
            throw new JIllegalOperationException(code,msg);
        }
    }
}
