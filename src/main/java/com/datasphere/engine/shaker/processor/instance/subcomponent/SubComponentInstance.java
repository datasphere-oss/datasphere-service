package com.datasphere.engine.shaker.processor.instance.subcomponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.runner.SubProcessRunner;
import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstance;
import com.datasphere.resource.manager.module.panel.buscommon.constant.PanelState;

import io.micronaut.core.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jusfoun.common.springmvc.exception.JIllegalOperationException;

/**
 * Title: SubComponentInstance
 * Description: 子流程类
 * @date 2017年6月19日 下午6:31:47
 */
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

//	public SubComponentInstance(ComponentInstance componentInstance,
//			ComponentInstanceSnapshotService componentInstanceSnapshotService,
//			ProcessInstanceService processInstanceService, ComponentService componentService) {
//		super(componentInstance);
//		this.componentInstanceSnapshotService = componentInstanceSnapshotService;
//		this.processInstanceService = processInstanceService;
//		this.componentService = componentService;
//		this.componentInstance = componentInstance;
//	}

	@Override
	protected void compute() throws Exception {
		String subPanelId = this.componentInstance.getSubPanelId();
//		assembleProcessInputDatasets();
		wrapParams();
		runProcess(subPanelId);
	}

	/**
	 * 
	 * @author kevin 2017年6月19日 下午6:27:05
	 * @param subPanel
	 * @return
	 * @throws Exception
	 */
	public String runProcess(String subPanel) throws Exception {
		List<Component> fromComponents = filterVersion(componentService.getBeginComponents(subPanel));
		List<Component> toComponents = filterVersion(componentService.getEndComponents(subPanel));
		List<Component> allComponents = filterVersion(componentService.getAllComponentsWithPanel(subPanel));
		return executeProcess(subPanel, fromComponents, toComponents, allComponents);
	}

	/**
	 * 
	 * @author kevin 2017年6月19日 下午6:26:51
	 * @param components
	 *            过滤版本
	 * @return
	 */
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
	
//	public void assembleProcessInputDatasets() throws Exception {
//		List<String> inputNames = getInputNames();
//		for (String inputName : inputNames) {
//			if(StringUtils.isBlank(inputName)) continue;
//			AssociationEndpoint endpoint = getParentOutputEndpointByInputName(inputName);
//			if (endpoint == null) {
//				continue;
//			}
//			Dataset dataset = null;
//			Component component = endpoint.getComponent();
//			
//			if (component != null) {
//				dataset = component.getOutput(endpoint.getName());
//			} else {
//				String pre_dataKey = "dataset_"  + endpoint.getComponent().getId();
//				dataset = dataAccessor.getDatasetMetadata(pre_dataKey);
//			}
////			dataset=getInput(inputName);
//			this.setInput(inputName, dataset);
//		}
//	}


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
					logger.info("======================子流程开始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝allsize" + allComponentsWithProcess.size());

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
					logger.info("======================子流程结束＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
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
		// TODO Auto-generated method stub
		if(null==inputsMap) throw new JIllegalOperationException("没有数据输入！");
	}

}
