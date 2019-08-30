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

package com.datasphere.engine.shaker.processor.runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datasphere.common.data.Dataset;
import com.datasphere.engine.shaker.processor.instance.AssociationEndpoint;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.engine.shaker.processor.stop.StopSingleInstance;
import com.datasphere.engine.shaker.workflow.panel.constant.PanelState;
import com.datasphere.server.connections.service.DataAccessor;

public class ProcessRunner {
	protected DataAccessor dataAccessor;
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Log lgr = LogFactory.getLog(ProcessRunner.class);
	protected volatile Map<String, Component> allComponentsMap = new HashMap<>();
	protected Map<String, Component> finishedComponentsMap = new ConcurrentHashMap<>();
	private boolean isError = false;

	private String processId;
	// private String panelId;
	private List<String> allComponentIdsWithProcess;
	private List<Component> fromComponents;
	private List<String> toComponentIds;
	private ProcessInstance instance;
	// private List<Component> allComponents;
	private ProcessRecordService recordService;
	private ComponentInstanceService componentInstanceService;
	private String userId;

	
	public void setComponentInstanceService(ComponentInstanceService componentInstanceService) {
		this.componentInstanceService = componentInstanceService;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ProcessRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(ProcessRecordService recordService) {
		this.recordService = recordService;
	}

	public void setAllComponentsMap(Map<String, Component> allComponentsMap) {
		this.allComponentsMap = allComponentsMap;
	}

	// public void setAllComponents(List<Component> allComponents) {
	// this.allComponents = allComponents;
	// }

	public Map<String, Component> getFinishedComponentsMap() {
		return finishedComponentsMap;
	}

	public void setFinishedComponentsMap(Map<String, Component> finishedComponentsMap) {
		this.finishedComponentsMap = finishedComponentsMap;
	}

	public ProcessInstance getInstance() {
		return instance;
	}

	public void setInstance(ProcessInstance instance) {
		this.instance = instance;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public void setAllComponentIdsWithProcess(List<String> allComponentIdsWithProcess) {
		this.allComponentIdsWithProcess = allComponentIdsWithProcess;
	}

	public void setFromComponents(List<Component> fromComponents) {
		this.fromComponents = fromComponents;
	}

	public void setToComponents(List<String> toComponentIds) {
		this.toComponentIds = toComponentIds;
	}

	public ProcessRunner(String panelId, String processId, DataAccessor dataAccessor) {
		// this.panelId=panelId;
		this.processId = processId;
		this.dataAccessor = dataAccessor;
	}

	public void run(CountDownLatch latch) throws Exception {
		execute(getComponentFromAll(fromComponents), false, latch);
	}

	public synchronized Component getComponentFromAll(Component c) throws Exception {
		c = allComponentsMap.get(c.getId());
		return c;

	}

	public synchronized List<Component> getComponentFromAll(List<Component> cs) throws Exception {
		List<Component> list = new ArrayList<>();
		for (int i = 0; i < cs.size(); i++) {
			Component c = getComponentFromAll(cs.get(i));
			if (null != c)
				list.add(c);
		}
		return list;

	}

	public void execute(List<Component> components, final boolean skip, CountDownLatch latch) throws Exception {
		for (Component c : components) {
			executor.submit(() -> {
                boolean f = skip;
                c.setProcessId(processId);
                c.setUserId(userId);

                if (!f) {
                    if (!allComponentIdsWithProcess.contains(c.getId())) return null;
                    List<Component> parents = getProcessComponentsFromParents(allComponentsMap.get(c.getId()).getParents());
                    if (!isToRun(parents))  return null;
                    try {
                        complete(c,latch);
                        allComponentsMap.get(c.getId()).setStatus(ComponentInstanceStatus.SUCCESS);
                    } catch (Exception e) {
                        allComponentsMap.get(c.getId()).setStatus(ComponentInstanceStatus.FAILURE);
                        isError = true;
                        f = true;
                        lgr.error("The component[name:" + c.getName() + ",id:" + c.getId() + "] run fail.", e);
                    }
                } else {
                    if (allComponentIdsWithProcess.contains(c.getId())) latch.countDown();
                    allComponentsMap.get(c.getId()).setStatus(ComponentInstanceStatus.FAILURE);
                    lgr.info("The component[name:" + c.getName() + ",id:" + c.getId() + "] skip.");
                }

                if (toComponentIds.contains(c.getId())) return null;
                List<Component> children = allComponentsMap.get(c.getId()).getChildren();
                if (children == null || children.size() == 0) return null;
                execute(children, f, latch);
                return null;
            });
		}
	}

	public void assembleInputDatasets(Component c) throws Exception {
		List<String> inputNames = c.getInputNames();
		for (String inputName : inputNames) {
			AssociationEndpoint endpoint = c.getParentOutputEndpointByInputName(inputName);
			if (endpoint == null) {
				continue;
			}
			Dataset dataset = null;
			Component component = finishedComponentsMap.get(endpoint.getComponent().getId());

			if (component != null) {
				dataset = component.getOutput(endpoint.getName());
			} else {
				
//				String pre_dataKey = endpoint.getComponent().getDataSetKey(endpoint.getName());
				String pre_dataKey=componentInstanceService.getComponentOutDataSetKey(endpoint.getComponent().getId()).get(endpoint.getName());
				dataset = dataAccessor.getDatasetMetadata(pre_dataKey);
			}
			c.setInput(inputName, dataset);
		}
	}

	public synchronized boolean isToRun(List<Component> components) {
		for (Component component : components) {
			if (!finishedComponentsMap.containsKey(component.getId())) {
				return false;
			}
		}
		return true;
	}

	public List<Component> getProcessComponentsFromParents(List<Component> parents) {
		List<Component> components = new ArrayList<>();
		for (Component c : parents) {
			if (allComponentIdsWithProcess.contains(c.getId())) {
				components.add(allComponentsMap.get(c.getId()));
			}
		}
		return components;
	}

	/**
	 *
	 * @param component
	 * @param latch
	 * @throws Exception
	 */
	public void complete(Component component,CountDownLatch latch) throws Exception {
		if (null != instance && StopSingleInstance.getInstances().get(instance.getPanelId())) {
			instance.setEndTime(new Date());
			instance.setStatus(PanelState.STOP);
			return;
		}
		synchronized (allComponentsMap) {
			if (null != allComponentsMap.get(component.getId()).getStatus()) {
				return;
			}
			if (finishedComponentsMap.containsKey(component.getId())) {
				return;
			}
			allComponentsMap.get(component.getId()).setStatus(ComponentInstanceStatus.RUNNING);
		}
		try{
			assembleInputDatasets(component);
			component.run(instance);
			finishedComponentsMap.put(component.getId(), component);
		} finally {
			latch.countDown();
		}
	}

}
