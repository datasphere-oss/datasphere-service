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

package com.datasphere.server.datasource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.server.domain.engine.EngineLoadService;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.Optional;

@Component
@Scope(SCOPE_PROTOTYPE)
@Transactional
@DisallowConcurrentExecution
public class DataSourceTemporaryJob extends QuartzJobBean {

  private static Logger LOGGER = LoggerFactory.getLogger(DataSourceTemporaryJob.class);

  @Autowired
  DataSourceTemporaryRepository temporaryRepository;

  @Autowired
  EngineLoadService engineLoadService;

  public DataSourceTemporaryJob() {
  }

  @Override
  public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    Trigger trigger = jobExecutionContext.getTrigger();
    JobDataMap jobData = trigger.getJobDataMap();

    Optional<DataSourceTemporary> dataSourceTemporary =
        temporaryRepository.findById(jobData.getString("temporaryId"));

    if(dataSourceTemporary == null) {
      LOGGER.warn("Job({}) - Fail to find temporary entity.", trigger.getKey().getName());
      return;
    }

    // Check Temporary DataSource!

//    engineLoadService.

    LOGGER.info("Job({}) - Successfully ingest incremental datasource.", trigger.getKey().getName());
  }
}
