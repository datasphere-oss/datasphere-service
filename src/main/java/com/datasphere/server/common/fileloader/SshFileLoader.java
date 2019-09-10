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

package com.datasphere.server.common.fileloader;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.datasphere.server.domain.engine.EngineProperties;
import com.datasphere.server.util.SshUtils;

import static java.util.stream.Collectors.toList;

@Component
public class SshFileLoader implements FileLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(SshFileLoader.class);

  @Override
  public List<String> put(FileLoaderProperties properties, String... targets) {
    return put(properties, Lists.newArrayList(targets), null, false);
  }

  @Override
  public List<String> put(FileLoaderProperties properties, List<String> sourcePaths, List<String> targetNames, boolean checkSrcFile) {

    Map<String, EngineProperties.Host> hosts = properties.getHosts();

    if (MapUtils.isEmpty(hosts)) {
      LOGGER.debug("This is localhost.");
      return sourcePaths;
    }

    String remoteHostname;
    EngineProperties.Host remoteInfo;
    String remoteDir = properties.getRemotePathWithoutScheme();
    for (String key : hosts.keySet()) {
      remoteHostname = key;
      remoteInfo = hosts.get(key);

      SshUtils.copyLocalToRemoteFileByScp(sourcePaths,
                                          targetNames,
                                          remoteDir,
                                          remoteHostname,
                                          remoteInfo.getPort(),
                                          remoteInfo.getUsername(),
                                          remoteInfo.getPassword(),
                                          checkSrcFile);
    }

    return Lists.newArrayList(sourcePaths).stream()
                .map(s -> properties.getRemoteDir() + File.separator + Paths.get(s).getFileName())
                .collect(toList());
  }

  @Override
  public List<String> get(FileLoaderProperties properties, String... file) {
    return null;
  }
}
