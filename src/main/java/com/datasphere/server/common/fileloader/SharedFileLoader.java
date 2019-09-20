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

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class SharedFileLoader implements FileLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(SharedFileLoader.class);
  
  @Override
  public List<String> put(FileLoaderProperties properties, String... paths) {
    return put(properties, Lists.newArrayList(paths), null, false);
  }

  @Override
  public List<String> put(FileLoaderProperties properties, List<String> sourcePaths, List<String> targetNames, boolean checkSrcPath) {

    List<String> loadPaths = Lists.newArrayList();
    for(int i=0; i<sourcePaths.size(); i++) {

      Path sourcePath = getPath(sourcePaths.get(i));

      if (!sourcePath.toFile().isFile()) {
        if(checkSrcPath) {
          throw new RuntimeException("No source file(" + sourcePath.toString() + ")");
        } else {
          LOGGER.warn("No source file({}) to copy. This file is passed.", sourcePath.toString());
          continue;
        }
      }

      boolean renameTargetFile = CollectionUtils.isNotEmpty(targetNames)
          && sourcePaths.size() == targetNames.size();

      String targetFileName = renameTargetFile ? targetNames.get(i) : sourcePath.getFileName().toString();

      String remotePathStr = properties.getRemotePath(targetFileName);
      Path remotePath = getPath(remotePathStr);

      if(sourcePath.compareTo(remotePath) != 0) {
        try {
          Files.copy(sourcePath, remotePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          LOGGER.error("Fail to copy local files to shared directory({})", remotePathStr);
          throw new RuntimeException("Fail to copy local files to shared directory : " + remotePathStr);
        }
      } else {
        LOGGER.debug("The source and remote files have the same path.");
      }
      LOGGER.debug("Successfully copy local files({}) to shared directory({})", sourcePath, remotePathStr);
      loadPaths.add(remotePathStr);
    }

    return loadPaths;
  }

  @Override
  public List<String> get(FileLoaderProperties properties, String... file) {
    return null;
  }

  public Path getPath(String path) {
    URI uri = URI.create(path);
    if(uri.getScheme() == null) {
      return Paths.get(URI.create("file://" + path));
    }
    return Paths.get(uri);
  }
}
