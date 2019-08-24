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

package com.datasphere.core.common.utils;

public class ReflectHelper
{
    public static <T> T getValue(final Object o, final String s) {
        throw new Error("Unresolved compilation problem: \n\tMulti-catch parameters are not allowed for source level below 1.7\n");
    }
    
    public static void setValue(final Object o, final String s, final Object o2) {
        throw new Error("Unresolved compilation problem: \n\tMulti-catch parameters are not allowed for source level below 1.7\n");
    }
    
    public static <T> T to(final Object obj, final Class<T> clz) {
        if (obj != null && clz.isAssignableFrom(obj.getClass())) {
            return (T)obj;
        }
        return null;
    }
}
