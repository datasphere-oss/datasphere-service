package com.datasphere.engine.manager.resource.provider;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Service
public class MyBatisSqlSessionFactoryService {
  private static final Logger log = LoggerFactory.getLogger(MyBatisSqlSessionFactoryService.class);

  @Autowired
  private DataSourceService dataSourceService;

  private SqlSessionFactory sqlSessionFactory;

  @PostConstruct
  public void initialize() {
    DataSource dataSource = dataSourceService.getDataSource();
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment  = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    configuration.addMappers("com.datasphere.engine.processor");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  public SqlSession getSqlSession() {
    return sqlSessionFactory.openSession();
  }
}
