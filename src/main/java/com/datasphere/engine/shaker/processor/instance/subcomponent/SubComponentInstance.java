package com.datasphere.engine.shaker.processor.instance.subcomponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.datasphere.common.data.Dataset;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.runner.SubProcessRunner;
import com.datasphere.engine.shaker.workflow.panel.constant.PanelState;


public class SubComponentInstance extends AbstractComponent {
	private final static Log logger = LogFactory.getLog(SubComponentInstance.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	private static final String INPUT_NAME="IN001";
	private static final String OUTPUT_NAME="OUT001";

	public SubComponentInstance(ComponentInstance componentInstance) {
		super(componentInstance);
		INPUT_NAMES.add(INPUT_NAME);
		OUTPUT_NAMES.add(OUTPUT_NAME);
	}


	@Override
	protected void compute() throws Exception {
		String subPanelId = this.componentInstance.getSubPanelId();
		wrapParams();
		runProcess(subPanelId);
	}

	public String runProcess(String subPanel) throws Exception {
		List<Component> fromComponents = filterVersion(componentService.getBeginComponents(subPanel));
		List<Component> toComponents = filterVersion(componentService.getEndComponents(subPanel));
		List<Component> allComponents = filterVersion(componentService.getAllComponentsWithPanel(subPanel));
		return executeProcess(subPanel, fromComponents, toComponents, allComponents);
	}

	public List<Component> filterVersion(List<Component> components) {
		String version = this.getVersion();
		List<Component> list = new ArrayList<>();
		for (Component c : components) {
			if (c.getVersion().equals(version)) {
				list.add(c);
			}
		}
		return list;
	}

	private List<String> getComponentIds(List<Component> components) {
		List<String> list = new ArrayList<String>();
		for (Component c : components) {
			list.add(c.getId());
		}
		return list;
	}
	

	private String executeProcess(final String subPanel, final List<Component> fromComponents, final List<Component> toComponents,
			final List<Component> allComponentsWithProcess) throws Exception {
		final String subProcessId = UUIDUtils.random();
		if(CollectionUtils.isEmpty(allComponentsWithProcess)){
			setAllOutputs(inputsMap.get(INPUT_NAME));
			return null;
		}
		executor.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				
				ProcessInstance processInstance = new ProcessInstance();
				processInstance.setId(subProcessId);
				processInstance.setPanelId(subPanel);
				List<String> fromComponentIds = getComponentIds(fromComponents);
				processInstance.setFromComponentInstanceIds(StringUtils.join(fromComponentIds, ","));
				List<String> toComponentIds = getComponentIds(toComponents);
				processInstance.setToComponentInstanceIds(StringUtils.join(toComponentIds, ","));
				processInstance.setStatus(PanelState.RUNNING);
				SubProcessRunner subRunner = new SubProcessRunner(subPanel, processInstance.getId(), dataAccessor);
				subRunner.setFromComponents(fromComponents);
				subRunner.setAllComponentIdsWithProcess(getComponentIds(allComponentsWithProcess));
				subRunner.setToComponents(toComponentIds);
				subRunner.setAllInput(inputsMap);
				subRunner.setBeginComponentIds(fromComponentIds);
				CountDownLatch latch = new CountDownLatch(allComponentsWithProcess.size());
				try {
					componentInstanceSnapshotService.createComponentInstanceSnapshotsByPanelId(subPanel,
							processInstance.getId());
					processInstance.setBeginTime(new Date());
					processInstanceService.add(processInstance);
					logger.info("a subprocess starts" + allComponentsWithProcess.size());

					subRunner.run(latch);
					processInstance.setStatus(PanelState.SUCCESS);

					latch.await();

					if (!subRunner.isError()) {
						logger.info("the subprocess[" + processInstance.getId() + "] runs successfully.");
						Component subLastOut=subRunner.getFinishedComponentsMap().get(toComponents.get(0).getId());
						for (String name : subLastOut.getOutputNames()) {
							Dataset dataset = subLastOut.getOutput(name);
							setAllOutputs(dataset);
						}
					} else {
						processInstance.setStatus(PanelState.FAILURE);

						logger.info("the subprocess[" + processInstance.getId() + "] runs fail.");
					}
					logger.info("the subprocess ends");
				} catch (Exception e) {
					processInstance.setStatus(PanelState.FAILURE);
					logger.error("the subprocess[" + processInstance.getId() + "] runs fail.", e);
					throw new Exception(e);
				} finally {
					processInstance.setEndTime(new Date());
					processInstanceService.modify(processInstance);
				}
				return null;
			}

		});

		return null;

	}

	@Override
	protected void wrapParams() throws Exception {
		if(null==inputsMap) throw new JIllegalOperationException("没有数据输入！");
	}

}
