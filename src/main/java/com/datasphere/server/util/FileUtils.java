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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 */
public class FileUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

  public static boolean existFile(Path filePath) {
    if(filePath == null) {
      return false;
    }

    return filePath.toFile().exists();
  }

  /**
   * Delete files.
   */
  public static void deleteFiles(List<String> files) {
    for (String file : files) {
      deleteFile(file);
    }
  }

  /**
   * Delete file.
   */
  public static void deleteFile(String file) {
    Path filePath = Paths.get(file);
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      LOGGER.warn("Fail to delete file : {}", file);
    }
  }

}
