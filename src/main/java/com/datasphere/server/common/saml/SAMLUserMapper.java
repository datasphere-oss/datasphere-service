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

package com.datasphere.server.common.saml;

import org.opensaml.saml2.core.Attribute;
import org.springframework.security.saml.SAMLCredential;

import java.util.Optional;

import com.datasphere.server.domain.user.User;

public abstract class SAMLUserMapper {
  public abstract User createUser(SAMLCredential credential);

  public String getAttributeValue(SAMLCredential credential, String attributeName){
    String returnValue = null;
    if(credential != null && credential.getAttributes() != null){
      Optional<Attribute> foundAttribute = credential.getAttributes().stream()
              .filter(attribute -> attribute.getName().equals(attributeName))
              .findFirst();

      if(foundAttribute.isPresent()){
        returnValue = credential.getAttributeAsString(foundAttribute.get().getName());
      }
    }

    return returnValue;
  }
}
