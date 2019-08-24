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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class O
{
    public static void log(final Object obj) {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(String.valueOf(sdf.format(new Date())) + " Thread(" + String.valueOf(Thread.currentThread().getId()) + ")--" + obj);
    }
    
    public static void json(final Object o) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(o));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
