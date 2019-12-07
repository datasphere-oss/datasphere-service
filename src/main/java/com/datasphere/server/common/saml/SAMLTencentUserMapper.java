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

import org.springframework.security.saml.SAMLCredential;

import com.datasphere.server.user.User;

public class SAMLTencentUserMapper extends SAMLUserMapper{

  @Override
  public User createUser(SAMLCredential samlCredential) {
    User tencentUser = new User();
    if(samlCredential != null){
    		tencentUser.setTel(getAttributeValue(samlCredential, "手机号"));
    		tencentUser.setFullName(getAttributeValue(samlCredential, "显示名"));
    		tencentUser.setEmail(getAttributeValue(samlCredential, "电子邮件"));
    }
    return tencentUser;
  }
}
