/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.dataconnection;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.datasphere.server.connections.jdbc.JdbcConnectInformation;
import com.datasphere.server.connections.jdbc.accessor.JdbcAccessor;
import com.datasphere.server.connections.jdbc.connector.JdbcConnector;
import com.datasphere.server.connections.jdbc.dialect.JdbcDialect;
import com.datasphere.server.connections.jdbc.exception.JdbcDataConnectionErrorCodes;
import com.datasphere.server.connections.jdbc.exception.JdbcDataConnectionException;
import com.datasphere.server.domain.dataconnection.connector.CachedUserJdbcConnector;

/**
 *
 */
@Component
@DependsOn("pluginManager")
public class DataConnectionHelper {

  @Autowired
  ApplicationContext applicationContext0;

  @Autowired
  List<JdbcDialect> jdbcDialects0;

  @Autowired
  List<JdbcConnector> jdbcConnectors0;

  @Autowired
  @Qualifier("CachedUserJdbcConnector")
  CachedUserJdbcConnector defaultConnector0;

  @Autowired
  PluginManager pluginManager0;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataConnectionHelper.class);
  private static ApplicationContext applicationContext;
  private static List<JdbcDialect> jdbcDialects;
  private static List<JdbcConnector> jdbcConnectors;
  private static CachedUserJdbcConnector defaultConnector;
  private static PluginManager pluginManager;

  @PostConstruct
  private void initStaticHelper () {
    applicationContext = this.applicationContext0;
    jdbcDialects = this.jdbcDialects0;
    jdbcConnectors = this.jdbcConnectors0;
    defaultConnector = this.defaultConnector0;
    pluginManager = this.pluginManager0;
  }

  public static JdbcAccessor getAccessor(JdbcConnectInformation connectInformation) throws JdbcDataConnectionException{
    JdbcDialect jdbcDialect = lookupDialect(connectInformation);
    JdbcConnector jdbcConnector = lookupJdbcConnector(connectInformation, jdbcDialect);

    JdbcAccessor jdbcDataAccessor = lookupJdbcDataAccessor(connectInformation, jdbcDialect);
    jdbcDataAccessor.setConnector(jdbcConnector);
    jdbcDataAccessor.setConnectionInfo(connectInformation);
    jdbcDataAccessor.setDialect(jdbcDialect);
    return jdbcDataAccessor;
  }

  public static JdbcDialect lookupDialect(JdbcConnectInformation connectInformation) throws JdbcDataConnectionException{
    return lookupDialect(connectInformation.getImplementor());
  }

  public static JdbcDialect lookupDialect(String implementor) throws JdbcDataConnectionException{
    JdbcDialect matchedDialect = null;

    //look up in bean list
    for(JdbcDialect dialect : jdbcDialects){
      if(dialect.isSupportImplementor(implementor)){
        matchedDialect = dialect;
        break;
      }
    }

    if(matchedDialect == null){
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.NOT_FOUND_SUITABLE_DIALECT,
                                            "not found suitable JdbcDialect for " + implementor);
    }

    return matchedDialect;
  }

  public static JdbcConnector lookupJdbcConnector(JdbcConnectInformation jdbcConnectInformation, JdbcDialect dialect){
    JdbcConnector matchedConnector = null;

    String definedConnectorClass = dialect.getConnectorClass(jdbcConnectInformation);
    LOGGER.debug("Look up Jdbc Connector for {}, cls : {}", jdbcConnectInformation.getImplementor(), definedConnectorClass);
    if(StringUtils.isNotEmpty(definedConnectorClass)){
      LOGGER.debug("check class name in connector bean list.");
      for(JdbcConnector connector : jdbcConnectors){
        LOGGER.debug("check class name jdbcConnectors : {}", connector.getClass().getName());
        if(connector.getClass().getName().equals(definedConnectorClass)){
          matchedConnector = connector;
          break;
        }
      }
    }

    if(matchedConnector == null){
      LOGGER.debug("matchedConnector not exist. return defaultConnector: {}", defaultConnector);
      return (JdbcConnector) defaultConnector;
    }

    LOGGER.debug("matchedConnector : {}", matchedConnector);
    return matchedConnector;
  }

  private static JdbcAccessor lookupJdbcDataAccessor(JdbcConnectInformation connectInformation, JdbcDialect dialect) throws JdbcDataConnectionException{

    JdbcAccessor matchedDataAccessor = null;

    String definedDataAccessorClass = dialect.getDataAccessorClass(connectInformation);

    List<Class<? extends JdbcAccessor>> extensionClass = pluginManager.getExtensionClasses(JdbcAccessor.class);

    try{
      for(Class<? extends JdbcAccessor> cls : extensionClass){
        if(cls.getTypeName().equals(definedDataAccessorClass)){
          matchedDataAccessor = cls.newInstance();
          break;
        }
      }
    } catch (InstantiationException | IllegalAccessException e){
      e.printStackTrace();
    }

    if(matchedDataAccessor == null){
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.NOT_FOUND_SUITABLE_DATA_ACCESSOR,
                                            "not found suitable Accessor for " + connectInformation.getImplementor());
    }

    return matchedDataAccessor;
  }

  public static String getConnectionUrl(JdbcConnectInformation connectInformation) throws JdbcDataConnectionException{
    return getConnectionUrl(connectInformation, connectInformation.getDatabase());
  }

  public static String getConnectionUrl(JdbcConnectInformation connectInformation, String database) throws JdbcDataConnectionException{
    return getConnectionUrl(connectInformation, database, true);
  }

  public static String getConnectionUrl(JdbcConnectInformation connectInformation, String database, boolean includeDatabase) throws JdbcDataConnectionException{
    JdbcDialect jdbcDialect = lookupDialect(connectInformation);
    return jdbcDialect.makeConnectUrl(connectInformation, database, includeDatabase);
  }
}
