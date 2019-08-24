package com.datasphere.engine.manager.resource.provider.database.service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.manager.resource.provider.database.dao.ElasticSearchDao;
import com.datasphere.engine.manager.resource.provider.database.dao.MysqlDao;
import com.datasphere.engine.manager.resource.provider.database.dao.OracleDao;
import com.datasphere.engine.manager.resource.provider.database.dao.PostgresDao;
import com.datasphere.server.manager.common.constant.ConnectionInfoAndOthers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DBOperationService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(DBOperationService.class);

    public boolean insertDatas(ConnectionInfoAndOthers connectionInfoAndOthers) throws Exception {
        boolean result = false;
        log.info("{}",connectionInfoAndOthers.getTypeName().toUpperCase());
        switch (connectionInfoAndOthers.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.insertDatas(connectionInfoAndOthers);
                break;
            case "MYSQL":
                MysqlDao mysqlDao = new MysqlDao();
                result = mysqlDao.insertDatas(connectionInfoAndOthers);
                break;
            case "POSTGRES":
                PostgresDao postgresDao = new PostgresDao();
                result = postgresDao.insertDatas(connectionInfoAndOthers);
                break;
            case "ELASTIC":
                ElasticSearchDao elasticSearchDao = new ElasticSearchDao();
                result = elasticSearchDao.insertDatas(connectionInfoAndOthers);
                break;
        }
        return result;
    }

    public String selectFields(ConnectionInfoAndOthers connectionInfoAndOthers) throws SQLException {
        String result = "";
        log.info("{}",connectionInfoAndOthers.getTypeName().toUpperCase());
        switch (connectionInfoAndOthers.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.selectFields(connectionInfoAndOthers);
                break;
            case "MYSQL":
                MysqlDao mysqlDao = new MysqlDao();
                result = mysqlDao.selectFields(connectionInfoAndOthers);
                break;
            case "POSTGRES":
                PostgresDao postgresDao = new PostgresDao();
                result = postgresDao.selectFields(connectionInfoAndOthers);
                break;
        }
        return result;
    }

    public Map<String, Object> selectDatas(ConnectionInfoAndOthers connectionInfoAndOthers) {
        Map<String, Object> result = new HashMap<>();
        log.info("{}",connectionInfoAndOthers.getTypeName().toUpperCase());
        switch (connectionInfoAndOthers.getTypeName().toUpperCase()) {
            case "ORACLE":
                OracleDao oracleDao = new OracleDao();
                result = oracleDao.selectDatas(connectionInfoAndOthers);
                break;
            case "MYSQL":
                MysqlDao mysqlDao = new MysqlDao();
                result = mysqlDao.selectDatas(connectionInfoAndOthers);
                break;
            case "POSTGRES":
                PostgresDao postgresDao = new PostgresDao();
                result = postgresDao.selectDatas(connectionInfoAndOthers);
                break;
        }
        return result;
    }
}