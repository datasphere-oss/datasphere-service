package com.datasphere.engine.manager.resource.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

@Service
public class DataSourceService {
  private static final Logger log = LoggerFactory.getLogger(DataSourceService.class);
  private HikariDataSource dataSource;

  @PostConstruct
  public void initialize() {
    log.info("Initialize data source....");
    HikariConfig config = new HikariConfig(System.getProperty("user.dir")+ File.separator+"dwresources"+File.separator+"hikari.properties");
    dataSource = new HikariDataSource(config);
  }

  public HikariDataSource getDataSource() {
    return dataSource;
  }


  @PreDestroy
  public void releaseResource() {
    log.info("release data source");
    if(dataSource != null) dataSource.close();
  }
}
