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

package com.datasphere.server.common.entity;

import com.fasterxml.jackson.databind.type.CollectionType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.datasphere.server.common.GlobalObjectMapper;

/**
 * Created by aladin on 2019. 7. 18..
 */
@Component
public class SpecValidator implements ConstraintValidator<Spec, String> {

  private static Logger LOGGER = LoggerFactory.getLogger(SpecValidator.class);

  private Class target;

  private boolean isArray;

  @Override
  public void initialize(Spec specAnnotation) {
    if(specAnnotation.target() == null) {
      throw new IllegalArgumentException("target class required.");
    }
    target = specAnnotation.target();
    isArray = specAnnotation.isArray();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    LOGGER.debug("Check spec for {} : {}", target, value);

    if(StringUtils.isEmpty(value)) {
      return true;
    }

    try {
      if(isArray) {
        CollectionType type = GlobalObjectMapper.getDefaultMapper()
                                                .getTypeFactory().constructCollectionType(List.class, target);
        GlobalObjectMapper.getDefaultMapper().readValue(value, type);
      } else {
        GlobalObjectMapper.getDefaultMapper().readValue(value, target);
      }
    } catch (IOException e) {
      LOGGER.warn("Invalid spec({}) : {}", target.getName(), e.getMessage());
      return false;
    }

    return true;
  }
}
