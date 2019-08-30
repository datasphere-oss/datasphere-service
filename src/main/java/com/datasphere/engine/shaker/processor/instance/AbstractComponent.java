package com.datasphere.engine.shaker.processor.instance;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datasphere.common.data.Dataset;
import com.datasphere.engine.common.message.CustomizedPropertyPlaceholderConfigurer;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.factory.ComponentFactory;
import com.datasphere.engine.shaker.processor.instance.callbackresult.ComponentCalcuateResult;
import com.datasphere.engine.shaker.processor.instance.componentparams.DefaultComponentParams;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.constant.DataKeyPrefix;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.instance.service.PreDataComponentService;
import com.datasphere.engine.shaker.processor.message.status.notice.CallBackStatusMessage;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.model.ProcessRecord;
import com.datasphere.engine.shaker.processor.service.ComponentInstanceSnapshotService;
import com.datasphere.engine.shaker.processor.service.ComponentService;
import com.datasphere.engine.shaker.processor.service.ProcessInstanceService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.engine.shaker.processor.stop.StopSingleInstance;
import com.datasphere.engine.shaker.workflow.panel.model.Panel;
import com.datasphere.engine.shaker.workflow.panel.service.PanelServiceImpl;
import com.datasphere.engine.shaker.workflow.panel.service.SubPanelService;
import com.datasphere.server.connections.service.DataAccessor;

public abstract class AbstractComponent implements Component {

	private final static Log logger = LogFactory.getLog(AbstractComponent.class);

	protected final List<String> INPUT_NAMES = new ArrayList<>();
	protected final List<String> OUTPUT_NAMES = new ArrayList<>();

	protected ComponentInstance componentInstance;
	private Map<String, AssociationEndpoint> parentsMap;
	private Map<String, List<AssociationEndpoint>> childrenMap;
	private Map<String, Dataset> outputsMap;

	protected ComponentInstanceService componentInstanceService;
	protected ComponentInstanceRelationService componentInstanceRelationService;
	private ComponentFactory componentFactory;
	private PanelServiceImpl panelService;
	protected String processId;
	protected Map<String, Dataset> inputsMap;
	protected DataAccessor dataAccessor;
	protected ProcessRecordService processRecordService;
	protected DefaultComponentParams params;
	private ProcessInstance instance;
	protected SubPanelService subPanelService;
	protected ComponentService componentService;
	protected ProcessInstanceService processInstanceService;
	protected ComponentInstanceSnapshotService componentInstanceSnapshotService;
	protected PreDataComponentService preDataComponentService;
	protected String jobId;
	protected long runMaxTime;
	protected CustomizedPropertyPlaceholderConfigurer propertiesBean;
	private String status;
	protected String userId;

	@Override
	public void setUserId(String userId) {
		// TODO Auto-generated method stub
		this.userId = userId;
	}

	@Override
	public void init() {
		assembleParentsMap();
		assembleChildrenMap();
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	@Override
	public void setStatus(String status) {
		// TODO Auto-generated method stub
		this.status = status;
	}

	public void setRunMaxTime(long runMaxTime) {
		this.runMaxTime = runMaxTime;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setPreDataComponentService(PreDataComponentService preDataComponentService) {
		this.preDataComponentService = preDataComponentService;
	}

	public void setProcessInstanceService(ProcessInstanceService processInstanceService) {
		this.processInstanceService = processInstanceService;
	}

	public void setComponentInstanceSnapshotService(ComponentInstanceSnapshotService componentInstanceSnapshotService) {
		this.componentInstanceSnapshotService = componentInstanceSnapshotService;
	}

	public void setComponentService(ComponentService componentService) {
		this.componentService = componentService;
	}

	public void setDataAccessor(DataAccessor dataAccessor) {
		this.dataAccessor = dataAccessor;
	}

	public void setPropertiesBean(CustomizedPropertyPlaceholderConfigurer propertiesBean) {
		this.propertiesBean = propertiesBean;
	}

	public void setComponentInstanceService(ComponentInstanceService componentInstanceService) {
		this.componentInstanceService = componentInstanceService;
	}

	public void setComponentInstanceRelationService(ComponentInstanceRelationService componentInstanceRelationService) {
		this.componentInstanceRelationService = componentInstanceRelationService;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public void setSubPanelService(SubPanelService subPanelService) {
		this.subPanelService = subPanelService;
	}

	public void setPanelService(PanelServiceImpl panelService) {
		this.panelService = panelService;
	}

	public void setProcessRecordService(ProcessRecordService processRecordService) {
		this.processRecordService = processRecordService;
	}

	public AbstractComponent(ComponentInstance componentInstance) {
		this.componentInstance = componentInstance;
	}

	@Override
	public String getDataSetKey(String name) {
		return (DataKeyPrefix.PREFIX_WORKFLOW + name + "_" + this.getId()).toLowerCase();
	}

	@Override
	public String getId() {
		return componentInstance.getId();
	}

	@Override
	public String getSubPanelId() {
		return componentInstance.getSubPanelId();
	}

	@Override
	public String getName() {
		return componentInstance.getCiName();
	}

	@Override
	public ComponentClassification getComponentType() {
		return componentInstance.getComponentType();
	}

	@Override
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	public String getVersion() {
		return componentInstance.getVersion();
	}

	@Override
	public List<Component> getParents() {
		assembleParentsMap();
		return getComponentsByAssociationEndpointsMap(parentsMap);
	}

	@Override
	public List<Component> getChildren() {
		assembleChildrenMap();
		return getComponentsByAssociationEndpointsListinMap(childrenMap);
	}

	@Override
	public List<String> getInputNames() {
		assembleParentsMap();

		return INPUT_NAMES;
	}

	@Override
	public List<String> getOutputNames() {
		assembleParentsMap();

		return OUTPUT_NAMES;
	}

	@Override
	public void setInput(String name, Dataset dataset) {
		if (inputsMap == null) {
			inputsMap = new HashMap<>();
		}
		inputsMap.put(name, dataset);
	}

	@Override
	public Dataset getOutput(String name) {
		if (outputsMap == null) {
			return null;
		}
		return outputsMap.get(name);
	}

	@Override
	public AssociationEndpoint getParentOutputEndpointByInputName(String inputName) {
		assembleParentsMap();
		return parentsMap.get(inputName);
	}

	@Override
	public Object getParamsStr() {
		return getFilterParamsStr(componentInstance.getCiParams());
	}

	public Object getFilterParamsStr(Object params) {
		return params;
	}

	@Override
	public void run(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		this.instance = instance;
		// setFinishedComponentsMap(finishedComponentsMap);
		run();
	}

	public void initOutDataSet() throws SQLException, Exception {
		for (int i = 0; i < OUTPUT_NAMES.size(); i++) {
			String dataKey = getDataSetKey(OUTPUT_NAMES.get(i));
			dataAccessor.removeDataset(dataKey);

		}
	}

	public ComponentCalcuateResult getStatusFromCSP(ComponentCalcuateResult calcuateResult) throws Exception {
		Date begin = new Date();
		do {
			Date end = new Date();
			long time = end.getTime() - begin.getTime();
			if (StopSingleInstance.getInstances().get(instance.getPanelId())) {
				calcuateResult.setStatus(ComponentInstanceStatus.FAILURE);
				calcuateResult.setMessage(String.valueOf(propertiesBean.get("ISSTOP")));
				CallBackStatusMessage.getInstance().remove(jobId);
				break;
			}
			if (time > this.runMaxTime) {
				if (null == calcuateResult)
					calcuateResult = new ComponentCalcuateResult();
				calcuateResult.setStatus(ComponentInstanceStatus.FAILURE);
				calcuateResult.setMessage(String.valueOf(propertiesBean.get("PROCESS_RUN_LONGTIME")));
				CallBackStatusMessage.getInstance().remove(jobId);
			}
			Object obj = CallBackStatusMessage.getInstance().get(jobId);
			if (obj instanceof ComponentCalcuateResult) {
				calcuateResult = (ComponentCalcuateResult) obj;
			} else {
				if (null == calcuateResult)
					calcuateResult = new ComponentCalcuateResult();
				calcuateResult.setStatus(ComponentInstanceStatus.FAILURE);
				calcuateResult.setMessage(String.valueOf(propertiesBean.get("PROCESS_RUN_LONGTIME")));
				CallBackStatusMessage.getInstance().remove(jobId);
				break;
			}
			Thread.sleep(1000);
		} while (calcuateResult.getStatus().equals(ComponentInstanceStatus.RUNNING));

		Date end = new Date();
		long time = end.getTime() - begin.getTime();
		logger.info(
				"组件id＝" + this.getId() + "===>运行状态：" + calcuateResult.getStatus() + "====>运行时间:" + time / 1000 + "s");
		CallBackStatusMessage.getInstance().remove(jobId);
		return calcuateResult;
	}

	@Override
	public void run() throws Exception {
//		setStatus(ComponentInstanceStatus.RUNNING);
		initOutDataSet();
		setRunMaxTime(Long.parseLong(String.valueOf(300000))); //getDefault()
		ProcessRecord record = processRecordService.create_back(processId, componentInstance.getPanelId(), getId(), userId);
		record.setBeginTime(new Date());
		record.setStatus(ComponentInstanceStatus.RUNNING);
		try {
			logger.info("the component[name:" + this.getName() + ",id:" + this.getId() + "] start to compute.");
			setJobId(record.getId());
			compute();
			record.setEndTime(new Date());
			record.setStatus(ComponentInstanceStatus.SUCCESS);
			// setStatus(ComponentInstanceStatus.SUCCESS);
			logger.info("the component[name:" + this.getName() + ",id:" + this.getId() + "] compute successfully.");
		} catch (Throwable e) {
			record.setEndTime(new Date());
			record.setStatus(ComponentInstanceStatus.FAILURE);
			// setStatus(ComponentInstanceStatus.FAILURE);
			record.setErrorMsg(e.getMessage());

			logger.error("the component[name:" + this.getName() + ",id:" + this.getId() + "] compute fail.", e);
			throw e;
		} finally {
			processRecordService.modify(record);
		}
	}

	private String getExceptionMsg(Throwable e) {
		StringBuilder sbr = new StringBuilder();
		StackTraceElement[] elements = e.getStackTrace();
		for (StackTraceElement element : elements) {
			sbr.append(element.toString()).append("\r\n");
		}
		return sbr.toString();
	}

	protected abstract void compute() throws Exception;

	protected abstract void wrapParams() throws Exception;

	protected void setOutput(String name, Dataset dataset) {
		if (outputsMap == null) {
			outputsMap = new HashMap<>();
		}
		outputsMap.put(name, dataset);
	}

	@Override
	public Dataset getInput(String name) {
		// TODO Auto-generated method stub
		return inputsMap.get(name);
	}

	protected void setAllOutputs(Dataset dataset) {
		List<String> outputNames = getOutputNames();
		for (String outputName : outputNames) {
			this.setOutput(outputName, dataset);
		}
	}

	private List<ComponentInstanceRelation> getSubRelations(String id) {
		List<ComponentInstanceRelation> relations = new ArrayList<>();
		// 查询当前实例
		ComponentInstance instance = componentInstanceService.getComponentInstanceById(id);
		// 查询所属面板
		Panel panel = panelService.getPanelById(instance.getPanelId());
		// 判断所属面板是否为子流程
		if (1 == panel.getType()) {
			// 当为子流程时，根据子流程面板的id和组件实例标示，查询出子流程的组建实例
			ComponentInstance subComponent = new ComponentInstance();
			subComponent.setSubPanelId(panel.getId());
			subComponent = componentInstanceService.getInstanceBySubPanelId(subComponent);
			// 查询目的地为subComponent组件的relation
			relations = componentInstanceRelationService
					.getComponentInstanceRelationsByDestinationId(subComponent.getId());

		}
		return relations;

	}

	private void assembleParentsMap() {
		if (parentsMap == null) {
			parentsMap = new HashMap<>();
			// 查询目的地为当前组件的relation
			List<ComponentInstanceRelation> relations = componentInstanceRelationService
					.getComponentInstanceRelationsByDestinationId(getId());
			for (ComponentInstanceRelation relation : relations) {
				ComponentInstance instance = componentInstanceService
						.getComponentInstanceById(relation.getSourceComponentInstanceId());
				// componentInstanceService.getEndComponentInstances(panelId)
				Component component = componentFactory.getInstance(instance, componentService);
				AssociationEndpoint endpoint = new AssociationEndpoint();
				endpoint.setName(relation.getSourceOutputName());
				endpoint.setComponent(component);
				parentsMap.put(relation.getDestInputName(), endpoint);
			}
		}
	}

	private void assembleChildrenMap() {
		if (childrenMap == null) {
			childrenMap = new HashMap<>();
			List<ComponentInstanceRelation> relations = componentInstanceRelationService
					.getComponentInstanceRelationsBySourceId(getId());
			for (ComponentInstanceRelation relation : relations) {
				ComponentInstance instance = componentInstanceService
						.getComponentInstanceById(relation.getDestComponentInstanceId());

				Component component = componentFactory.getInstance(instance, componentService);
				AssociationEndpoint endpoint = new AssociationEndpoint();
				endpoint.setName(relation.getDestInputName());
				endpoint.setComponent(component);
				List<AssociationEndpoint> endpoints = childrenMap.get(relation.getSourceOutputName());
				if (endpoints == null) {
					endpoints = new ArrayList<>();
					childrenMap.put(relation.getSourceOutputName(), endpoints);
				}
				endpoints.add(endpoint);
			}
		}
	}

	private List<Component> getComponentsByAssociationEndpointsMap(Map<String, AssociationEndpoint> map) {
		List<Component> components = new ArrayList<>();
		for (Map.Entry<String, AssociationEndpoint> entry : map.entrySet()) {
			if (!isEqualComponent(entry.getValue().getComponent(), components)) {
				components.add(entry.getValue().getComponent());
			}
		}
		return components;
	}

	private List<Component> getComponentsByAssociationEndpointsListinMap(Map<String, List<AssociationEndpoint>> map) {
		List<Component> components = new ArrayList<>();
		for (Map.Entry<String, List<AssociationEndpoint>> entry : map.entrySet()) {
			List<AssociationEndpoint> endpoints = entry.getValue();
			for (AssociationEndpoint endpoint : endpoints) {

				if (!isEqualComponent(endpoint.getComponent(), components)) {
					components.add(endpoint.getComponent());
				}

			}
		}
		return components;
	}

	private boolean isEqualComponent(Component component, List<Component> components) {
		for (Component c : components) {
			if (component.getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

}
