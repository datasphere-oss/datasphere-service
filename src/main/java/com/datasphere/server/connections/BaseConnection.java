package com.datasphere.server.connections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.server.connections.constant.ConnectionInfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据连接
 */
public class BaseConnection {

    private static final Logger log = LoggerFactory.getLogger(BaseConnection.class);

    /**
     * 取得数据连接
     * @param connInfo
     * @return
     */
    public static Connection getConnection(ConnectionInfo connInfo) {
        Connection conn = null;
        String url;
        try {
            switch (connInfo.getTypeName()) {
                case DatabaseConstants.DATABASE_ORACLE:
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    url =
                            "jdbc:oracle:thin:@" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + ":" +
                                    connInfo.getDatabaseName();

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
                case DatabaseConstants.DATABASE_MYSQL:
                    Class.forName("com.mysql.jdbc.Driver");
                    url =
                            "jdbc:mysql://" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + "/" +
                                    connInfo.getDatabaseName()+"?useUnicode=true&characterEncoding=UTF-8";

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
                case DatabaseConstants.DATABASE_POSTGRES:
                    Class.forName("org.postgresql.Driver");

                    url =
                            "jdbc:postgresql://" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + "/" +
                                    connInfo.getDatabaseName();

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
            }

        } catch (Exception ex) {
            log.error("can not create connection: {}", ex);
            conn = null;
        }
        return conn;
    }

}
