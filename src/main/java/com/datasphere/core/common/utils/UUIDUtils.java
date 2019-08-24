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

import java.util.UUID;

public class UUIDUtils
{
    public static String random() {
        final UUID uuid = UUID.randomUUID();
        return String.valueOf(digits(uuid.getMostSignificantBits() >> 32, 8)) + digits(uuid.getMostSignificantBits() >> 16, 4) + digits(uuid.getMostSignificantBits(), 4) + digits(uuid.getLeastSignificantBits() >> 48, 4) + digits(uuid.getLeastSignificantBits(), 12);
    }
    
    private static String digits(final long paramLong, final int paramInt) {
        final long l = 1L << paramInt * 4;
        return Long.toHexString(l | (paramLong & l - 1L)).substring(1);
    }
}
