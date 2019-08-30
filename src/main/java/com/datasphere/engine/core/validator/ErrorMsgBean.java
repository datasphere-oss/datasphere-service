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

package com.datasphere.engine.core.validator;

public class ErrorMsgBean
{
    StringBuffer sb;
    int count;
    
    public ErrorMsgBean() {
        this.sb = new StringBuffer();
        this.count = 0;
    }
    
    public void append(final String msg) {
        this.sb.append(String.valueOf(++this.count) + ".");
        this.sb.append(msg);
        this.sb.append("\n");
    }
    
    public String toString() {
        return this.sb.toString();
    }
    
    public boolean hasMessage() {
        return this.count != 0;
    }
}
