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

package com.datasphere.server.util.dialect;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

import java.util.List;

/**
 * Created by kyungtaak on 2016. 4. 6..
 */
public class MySQLBitwiseAndFunction extends StandardSQLFunction {

  public MySQLBitwiseAndFunction(String name) {
    super(name);
  }

  public MySQLBitwiseAndFunction(String name, Type typeValue) {
    super(name, typeValue);
  }

  @Override
  public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
    if (arguments.size() != 2){
      throw new IllegalArgumentException("the function must be passed 2 arguments");
    }

    final StringBuilder buf = new StringBuilder();
    buf.append(arguments.get(0))
            .append(" & ")
            .append(arguments.get(1));

    return buf.toString();
  }

}
