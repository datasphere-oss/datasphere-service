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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.datasphere.server.common.fileloader.FileLoaderProperties.RemoteType.SSH;

@Component
public class FileLoaderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderFactory.class);

  @Autowired
  SshFileLoader sshFileLoader;

  @Autowired
  SharedFileLoader sharedFileLoader;

  @Autowired
  LocalFileLoader localFileLoader;

  public FileLoaderFactory() {
  }

  private FileLoader getFileLoader(FileLoaderProperties.RemoteType remoteType) {
    switch (remoteType) {
      case SSH:
        return sshFileLoader;
      case SHARED:
        return sharedFileLoader;
      case LOCAL:
        return localFileLoader;
      case HDFS:
      default:
        throw new RuntimeException("Not support type of file loader");
    }
  }

  public List<String> put(FileLoaderProperties properties, String... paths) {
    return getFileLoader(properties.getRemoteType()).put(properties, paths);
  }

  public List<String> put(String targetHostName, FileLoaderProperties properties, String... paths) {
    return put(targetHostName, properties, Lists.newArrayList(paths), null, false);
  }

  public List<String> put(String targetHostName, FileLoaderProperties properties, List<String> sourcePaths, List<String> targetNames, boolean checkSrcFile) {
    if(properties.getRemoteType() == SSH && StringUtils.isNotEmpty(targetHostName)) {
      properties = properties.targetHostProperties(targetHostName);
    }
    return getFileLoader(properties.getRemoteType()).put(properties, sourcePaths, targetNames, checkSrcFile);
  }

  public List<String> get(FileLoaderProperties properties, String... file) {
    return getFileLoader(properties.getRemoteType()).get(properties, file);
  }
}
