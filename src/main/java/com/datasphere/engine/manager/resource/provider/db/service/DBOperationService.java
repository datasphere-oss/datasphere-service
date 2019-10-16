package com.datasphere.engine.manager.resource.provider.db.service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.datasource.connections.constant.ConnectionInfo;
import com.datasphere.engine.manager.resource.provider.db.dao.ElasticSearchDao;
import com.datasphere.engine.manager.resource.provider.db.dao.MySQLDao;
import com.datasphere.engine.manager.resource.provider.db.dao.OracleDao;
import com.datasphere.engine.manager.resource.provider.db.dao.PostgreSQLDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DBOperationService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(DBOperationService.class);

    public boolean insertDatas(ConnectionInfo connectionInfo) throws Exception {
        boolean result = false;
        log.info("{}",connectionInfo.getTypeName().toUpperCase());
        switch (connectionInfo.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.insertDatas(connectionInfo);
                break;
            case "MYSQL":
                MySQLDao mySQLDao = new MySQLDao();
                result = mySQLDao.insertDatas(connectionInfo);
                break;
            case "POSTGRES":
                PostgreSQLDao postgreSQLDao = new PostgreSQLDao();
                result = postgreSQLDao.insertDatas(connectionInfo);
                break;
            case "ELASTIC":
                ElasticSearchDao elasticSearchDao = new ElasticSearchDao();
                result = elasticSearchDao.insertDatas(connectionInfo);
                break;
        }
        return result;
    }

    public String selectFields(ConnectionInfo connectionInfo) throws SQLException {
        String result = "";
        log.info("{}",connectionInfo.getTypeName().toUpperCase());
        switch (connectionInfo.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.selectFields(connectionInfo);
                break;
            case "MYSQL":
                MySQLDao mySQLDao = new MySQLDao();
                result = mySQLDao.selectFields(connectionInfo);
                break;
            case "POSTGRES":
                PostgreSQLDao postgreSQLDao = new PostgreSQLDao();
                result = postgreSQLDao.selectFields(connectionInfo);
                break;
        }
        return result;
    }

    public Map<String, Object> selectDatas(ConnectionInfo connectionInfo) {
        Map<String, Object> result = new HashMap<>();
        log.info("{}",connectionInfo.getTypeName().toUpperCase());
        switch (connectionInfo.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.selectDatas(connectionInfo);
                break;
            case "MYSQL":
                MySQLDao mySQLDao = new MySQLDao();
                result = mySQLDao.selectDatas(connectionInfo);
                break;
            case "POSTGRES":
                PostgreSQLDao postgreSQLDao = new PostgreSQLDao();
                result = postgreSQLDao.selectDatas(connectionInfo);
                break;
        }
        return result;
    }
}