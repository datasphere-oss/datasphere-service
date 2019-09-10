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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import com.datasphere.server.domain.engine.EngineProperties;

public class FileLoaderProperties implements Serializable {

  RemoteType remoteType;

  String localBaseDir;

  String remoteDir;

  /**
   * SSH type 일 경우, 관련 Host 기입 <br/>
   * Key 에 위치할 hostname 이 "." 포함된 경우, "[test.com]" 로 기입
   */
  Map<String, EngineProperties.Host> hosts = Maps.newHashMap();

  public FileLoaderProperties() {
  }

  public FileLoaderProperties(RemoteType remoteType, String localBaseDir, String remoteDir, Map<String, EngineProperties.Host> hosts) {
    Preconditions.checkArgument(remoteType != null, "remoteType required.");
    Preconditions.checkArgument(StringUtils.isNotEmpty(localBaseDir), "localBaseDir required.");

    this.remoteType = remoteType;
    this.localBaseDir = localBaseDir;
    this.remoteDir = remoteDir;
    this.hosts = hosts;
  }

  public FileLoaderProperties targetHostProperties(String targetHost) {
    if(!hosts.containsKey(targetHost)) {
      throw new IllegalArgumentException("Target hostname(" + targetHost + ") not found in " + hosts.keySet() + ". Check hostname.");
    }

    // Rewrite only selected hosts map.
    Map<String, EngineProperties.Host> selectedHosts = Maps.newHashMap();
    selectedHosts.put(targetHost, hosts.get(targetHost));

    return new FileLoaderProperties(remoteType, localBaseDir, remoteDir, selectedHosts);
  }

  public String getRemotePathWithScheme() {
    URI uri = URI.create(getRemoteDir());
    if(uri.getScheme() == null) {
      return "file:///" + uri.getPath();
    }
    return uri.getPath();
  }

  public String getRemotePathWithoutScheme() {
    URI uri = URI.create(getRemoteDir());
    return uri.getPath();
  }

  public String getRemotePath(String fileName) {
    return getRemoteDir() + File.separator + fileName;
  }

  public RemoteType getRemoteType() {
    return remoteType;
  }

  public void setRemoteType(RemoteType remoteType) {
    this.remoteType = remoteType;
  }

  public String getLocalBaseDir() {
    return localBaseDir;
  }

  public URI getLocalBaseUri() {
    return URI.create(getLocalBaseDir());
  }

  public void setLocalBaseDir(String localBaseDir) {
    this.localBaseDir = localBaseDir;
  }

  public String getRemoteDir() {
    if(remoteDir == null) {
      return localBaseDir;
    }

    return remoteDir;
  }

  public void setRemoteDir(String remoteDir) {
    this.remoteDir = remoteDir;
  }

  public Map<String, EngineProperties.Host> getHosts() {
    return hosts;
  }

  public void setHosts(Map<String, EngineProperties.Host> hosts) {
    this.hosts = hosts;
  }

  @Override
  public String toString() {
    return "FileLoaderProperties{" +
        "remoteType=" + remoteType +
        ", localBaseDir='" + localBaseDir + '\'' +
        ", remoteDir='" + remoteDir + '\'' +
        ", hosts=" + hosts +
        '}';
  }

  public enum RemoteType {
    LOCAL, SSH, SHARED, HDFS
  }
}
