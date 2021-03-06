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

package com.datasphere.server.domain.workbench.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.datasphere.engine.datasource.connections.DataConnection;
import com.datasphere.engine.datasource.connections.DataConnectionHelper;
import com.datasphere.engine.datasource.connections.DataConnectionRepository;
import com.datasphere.engine.datasource.connections.jdbc.connector.JdbcConnector;
import com.datasphere.engine.datasource.connections.jdbc.dialect.JdbcDialect;
import com.datasphere.engine.datasource.connections.jdbc.exception.JdbcDataConnectionException;
import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.user.User;
import com.datasphere.server.user.service.CachedUserService;
import com.datasphere.server.util.AuthUtils;

/**
 * The type Work bench data source utils.
 */
@Component
public class WorkbenchDataSourceManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkbenchDataSourceManager.class);

  private Map<String, WorkbenchDataSource> pooledDataSourceList = new HashMap<>();

  @Autowired
  DataConnectionRepository dataConnectionRepository;

  @Autowired
  CachedUserService cachedUserService;

  public WorkbenchDataSource findDataSourceInfo(String webSocketId){
    WorkbenchDataSource dataSourceInfo = pooledDataSourceList.get(webSocketId);
    if(dataSourceInfo != null){
      LOGGER.debug("Created datasourceInfo Existed : {} ", webSocketId);
      return dataSourceInfo;
    } else {
      LOGGER.debug("Created datasourceInfo Not Existed : {} ", webSocketId);
      return null;
    }
  }

  /**
   * Destroy data source.
   *
   * @param webSocketId the web socket id
   * @throws JdbcDataConnectionException the jdbc data connection exception
   */
  public void destroyDataSource(String webSocketId) throws JdbcDataConnectionException {
    Assert.isTrue(!webSocketId.isEmpty(), "webSocketId Required.");

    WorkbenchDataSource dataSourceInfo = pooledDataSourceList.get(webSocketId);
    if(dataSourceInfo != null){
      pooledDataSourceList.remove(webSocketId);
      LOGGER.debug("datasource Destroy : {} - {}", dataSourceInfo.getConnectionId(), webSocketId);
      dataSourceInfo.destroy();
      dataSourceInfo = null;
    }
  }

  /**
   * Create data source info single connection data source info.
   *
   * @param dataConnection    the connection
   * @param webSocketId   the web socket id
   * @return the single connection data source info
   * @throws JdbcDataConnectionException the jdbc data connection exception
   */
  private WorkbenchDataSource createDataSourceInfo(DataConnection dataConnection,
                                                  String webSocketId,
                                                  String username,
                                                  String password) throws JdbcDataConnectionException{
    JdbcDialect jdbcDialect = DataConnectionHelper.lookupDialect(dataConnection);
    JdbcConnector jdbcConnector = DataConnectionHelper.lookupJdbcConnector(dataConnection, jdbcDialect);

    WorkbenchDataSource dataSourceInfo = new WorkbenchDataSource(dataConnection.getId(), webSocketId, dataConnection, jdbcConnector);
    dataSourceInfo.setUsername(username);
    dataSourceInfo.setPassword(password);

    pooledDataSourceList.put(webSocketId, dataSourceInfo);
    return dataSourceInfo;
  }

  public WorkbenchDataSource createDataSourceInfo(DataConnection connection,
                                                  String webSocketId) throws JdbcDataConnectionException{
    return createDataSourceInfo(connection, webSocketId, connection.getUsername(), connection.getPassword());
  }

  public Map<String, WorkbenchDataSource> getCurrentConnections(){
    return pooledDataSourceList;
  }
  // 根据用户名和密码获取用户的数据源
  public WorkbenchDataSource getWorkbenchDataSource(String dataConnectionId, String webSocketId, String username, String password) throws ResourceNotFoundException, BadRequestException, JdbcDataConnectionException {
    // 首先查询数据源信息
	WorkbenchDataSource dataSource = this.findDataSourceInfo(webSocketId);
    if(dataSource == null){
      DataConnection dataConnection = dataConnectionRepository.findById(dataConnectionId).get();
      if(dataConnection == null){
        throw new ResourceNotFoundException("DataConnection(" + dataConnectionId + ")");
      }

      dataSource = this.getWorkbenchDataSource(dataConnection, webSocketId, username, password);
    }
    return dataSource;
  }
  // 获得数据源
  public WorkbenchDataSource getWorkbenchDataSource(DataConnection jdbcDataConnection, String webSocketId, String username, String password) throws ResourceNotFoundException, BadRequestException, JdbcDataConnectionException{
    WorkbenchDataSource dataSource = this.findDataSourceInfo(webSocketId);
    if(dataSource == null){
      String connectionUsername;
      String connectionPassword;
      // 获得认证类型
      DataConnection.AuthenticationType authenticationType = jdbcDataConnection.getAuthenticationType();
      if(authenticationType == null){
        authenticationType = DataConnection.AuthenticationType.MANUAL;
      }
      // 获得已认证的用户名和密码
      switch (authenticationType){
        case USERINFO:
          connectionUsername = AuthUtils.getAuthUserName();
          User user = cachedUserService.findUser(connectionUsername);
          if(user == null){
            throw new ResourceNotFoundException("User(" + connectionUsername + ")");
          }
          connectionPassword = cachedUserService.findUser(connectionUsername).getPassword();
          break;
        // 数据源连接的用户名和密码
        case MANUAL:
          connectionUsername = jdbcDataConnection.getUsername();
          connectionPassword = jdbcDataConnection.getPassword();
          break;
        default:
          if(StringUtils.isEmpty(username)){
            throw new BadRequestException("Empty username");
          }
          if(StringUtils.isEmpty(password)){
            throw new BadRequestException("Empty password");
          }
          connectionUsername = username;
          connectionPassword = password;
          break;
      }

      dataSource = this.createDataSourceInfo(jdbcDataConnection, webSocketId, connectionUsername, connectionPassword);
    }
    return dataSource;
  }
}
