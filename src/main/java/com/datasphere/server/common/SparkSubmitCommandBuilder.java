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

package com.datasphere.server.common;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aladin on 2019. 7. 20..
 */
public class SparkSubmitCommandBuilder extends AbstractCommandBuilder {

    static final String SPARK_SUBMIT = "spark-submit";
    static final String SPARK_MASTER = "--master";
    static final String SPARK_DEPLOY_MODE = "--deploy-mode";
    static final String SPARK_DRIVER_MEMORY = "--driver-memory";
    static final String SPARK_EXECUTOR_MEMORY = "--executor-memory";
    static final String SPARK_EXECUTOR_CORES = "--executor-cores";
    static final String SPARK_NUM_EXECUTORS = "--num-executors";
    static final String SPARK_QUEUE = "--queue";

    String home;
    String master;
    String deployMode;
    String driverMemory;
    String executorMemory;
    String numExecutors;
    String executorCores;
    String queue;
    String execute;

    public SparkSubmitCommandBuilder() {
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDeployMode() {
        return deployMode;
    }

    public void setDeployMode(String deployMode) {
        this.deployMode = deployMode;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public String getNumExecutors() {
        return numExecutors;
    }

    public void setNumExecutors(String numExecutors) {
        this.numExecutors = numExecutors;
    }

    public String getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(String executorCores) {
        this.executorCores = executorCores;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    /**
     *
     */
    private void buildCommand() {
        List<String> appArgs = new ArrayList<>();
        appArgs.add(this.home + SPARK_SUBMIT);
        appArgs.add(SPARK_MASTER);
        appArgs.add(this.master);
        appArgs.add(SPARK_DEPLOY_MODE);
        appArgs.add(this.deployMode);
        applyOption(appArgs);
        appArgs.add(this.execute);
        super.commandArgs = appArgs;
    }

    /**
     *
     * @param appArgs
     */
    private List<String> applyOption(List<String> appArgs) {
        if(!StringUtils.isEmpty(this.driverMemory)){
            appArgs.add(SPARK_DRIVER_MEMORY);
            appArgs.add(this.driverMemory);
        } else if(!StringUtils.isEmpty(this.executorMemory)) {
            appArgs.add(SPARK_EXECUTOR_MEMORY);
            appArgs.add(this.executorMemory);
        } else if(!StringUtils.isEmpty(this.executorCores)) {
            appArgs.add(SPARK_EXECUTOR_CORES);
            appArgs.add(this.executorCores);
        } else if(!StringUtils.isEmpty(this.numExecutors)) {
            appArgs.add(SPARK_NUM_EXECUTORS);
            appArgs.add(this.numExecutors);
        } else if(!StringUtils.isEmpty(this.queue)) {
            appArgs.add(SPARK_QUEUE);
            appArgs.add(this.queue);
        }
        return appArgs;
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void run() throws Exception {
        buildCommand();
        super.run();
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public String runRedirectOutput() throws Exception {
        buildCommand();
        return super.runRedirectOutput();
    }

}
