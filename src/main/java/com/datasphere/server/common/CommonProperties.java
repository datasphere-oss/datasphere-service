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

package com.datasphere.server.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Component
@ConfigurationProperties(prefix="datasphere.common")
public class CommonProperties {

  Map<String, URI> manualLinks;

  public CommonProperties() {
  }

  public Optional<URI> getLinkByLang(String lang) {
    if(manualLinks == null) {
      return Optional.empty();
    }

    if(manualLinks.containsKey(lang)) {
      return Optional.of(manualLinks.get(lang));
    }

    return Optional.empty();
  }

  public Map<String, URI> getManualLinks() {
    return manualLinks;
  }

  public void setManualLinks(Map<String, URI> manualLinks) {
    this.manualLinks = manualLinks;
  }

}
