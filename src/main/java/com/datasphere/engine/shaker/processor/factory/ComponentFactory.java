package com.datasphere.engine.shaker.processor.factory;

import org.springframework.beans.factory.annotation.Autowired;

import com.datasphere.engine.common.message.CustomizedPropertyPlaceholderConfigurer;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.instance.datasource.SimpleDataSourceComponent;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.predatacomponent.PreDataComponentInstance;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.instance.service.PreDataComponentService;
import com.datasphere.engine.shaker.processor.instance.validation.AbstractComponentValition;
import com.datasphere.engine.shaker.processor.instance.validation.IBaseValidation;
import com.datasphere.engine.shaker.processor.instance.validation.instances.DefaultValidationInstances;
import com.datasphere.engine.shaker.processor.instance.validation.instances.SplitValidationInstances;
import com.datasphere.engine.shaker.processor.service.ComponentInstanceSnapshotService;
import com.datasphere.engine.shaker.processor.service.ComponentService;
import com.datasphere.engine.shaker.processor.service.ProcessInstanceService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.engine.shaker.workflow.panel.service.PanelServiceImpl;
import com.datasphere.engine.shaker.workflow.panel.service.SubPanelService;
import com.datasphere.server.connections.service.DataAccessor;

public class ComponentFactory {

	@Autowired
	private ComponentInstanceService componentInstanceService;
	@Autowired
	private ComponentInstanceRelationService componentInstanceRelationService;
	@Autowired
	private ProcessRecordService processRecordService;
	@Autowired
	private ProcessInstanceService processInstanceService;
	@Autowired
	private ComponentInstanceSnapshotService componentInstanceSnapshotService;
	@Autowired
	private PanelServiceImpl panelService;

	private DataAccessor dataAccessor;
	private CustomizedPropertyPlaceholderConfigurer propertiesBean;
	@Autowired
	private SubPanelService subPanelService;
	@Autowired
	private PreDataComponentService preDataComponentService;
	private IBaseValidation baseValidation;
	
	public Component getInstance(ComponentInstance componentInstance, ComponentService componentService) {
		AbstractComponent component = null;
		if (ComponentClassification.SimpleDataSource == componentInstance.getComponentType()) {
			component = new SimpleDataSourceComponent(componentInstance);
//		} else if (ComponentClassification.DataPreProcess == componentInstance.getComponentType()) {
		} else {
			component = new PreDataComponentInstance(componentInstance);
			component.setSubPanelService(subPanelService);
			component.setComponentService(componentService);
			component.setPreDataComponentService(preDataComponentService);
		}
		/*else { //jeq 目前不用
			AbstractComponentValition componentValition = validation(componentInstance);
			if (null != componentValition && componentValition.isErrorFlag()) {
				System.out.println(componentValition.getErrorMessage().toString());
				throw new JIllegalOperationException(componentValition.getErrorMessage().toString());
			}
			component = new DefaultComponentInstance(componentValition.getInstance());
			
		}*/
		if (component != null) {
			component.setComponentInstanceService(componentInstanceService);
			component.setComponentInstanceRelationService(componentInstanceRelationService);
			component.setComponentFactory(this);
			component.setDataAccessor(dataAccessor);
			component.setProcessRecordService(processRecordService);
			component.setPanelService(panelService);
			component.setPropertiesBean(propertiesBean);//jeq
			component.setProcessInstanceService(processInstanceService);
			component.setComponentInstanceSnapshotService(componentInstanceSnapshotService);
		}
		return component;
	}

	public AbstractComponentValition validation(ComponentInstance ci) {
		AbstractComponentValition componentValition = null;
		switch (ci.getComponentType()) {
		case Kmeans:
//			componentValition = new KmeansValitionInstances();
			break;
		case Join:
//			componentValition = new JoinValidationInstances();
			break;
		case Apriori:
//			componentValition = new AprioriValidationInstances();
			break;
		case FPGrowth:
//			componentValition=new FPGrowthValidationInstances();
			break;
		case NaiveBayes:
//			componentValition=new NaiveBayesValidationInstances();
			break;
		case RandomForest:
//			componentValition=new RandomForestValidationInstances();
			break;
		case Split:
			componentValition=new SplitValidationInstances();
			break;
		case SVM:
//			componentValition=new SVMValidationInstances();
			break;
		case XGBoost:
//			componentValition=new XGBoostValidationInstances();
			break;
		default:
			componentValition = new DefaultValidationInstances();
			break;
		}
		if (null != componentValition) {
			componentValition.setBaseValidation(baseValidation);
			componentValition.setInstance(ci);
		}
		return componentValition;
	}

}
