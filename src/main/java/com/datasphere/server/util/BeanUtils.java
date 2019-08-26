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

package com.datasphere.server.util;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {

  public static void copyPropertiesNullAware(Object dest, Object source)  {

    try{
      PropertyUtils.describe(source).entrySet().stream()
              .filter(element -> element.getValue() != null)
              .filter(element -> ! element.getKey().equals("class"))
              .forEach(element -> {
                try {
                  PropertyUtils.setProperty(dest, element.getKey(), element.getValue());
                } catch (Exception e) {
                  // Error setting property ...;
                }
              });
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
