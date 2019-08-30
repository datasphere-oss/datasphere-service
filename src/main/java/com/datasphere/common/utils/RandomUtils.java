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

package com.datasphere.common.utils;

import java.util.Random;

public class RandomUtils {

    /**
     * Generate a string of 16 digits
     * @return
     */
    public static String getNumStr_16() {
        Random random = new Random();
        boolean flag = true;
        String str_16 = "";
        while(flag) {
            int first = random.nextInt(99999999);
            int second = random.nextInt(99999999);
            long result = first * 100000000L + second;
            if (result > 1000000000000000L && result < 9999999999999999L) {
                flag = false;
                str_16 = String.valueOf(result);
            }
        }
        return str_16;
    }

    /**
     * Generate a string of 13 digits
     * @return
     */
    public static String getNumStr_13() {
        Random random = new Random();
        boolean flag = true;
        String str_13 = "";
        while(flag) {
            int first = random.nextInt(999999);
            int second = random.nextInt(999999);
            long result = first * 100000000L + second;
            if (result > 1000000000000L && result < 9999999999999L) {
                flag = false;
                str_13 = String.valueOf(result);
            }
        }
        return str_13;
    }
}
