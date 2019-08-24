package com.datasphere.engine.shaker.processor.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.datasphere.core.common.BaseEntity;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.common.message.CustomizedPropertyPlaceholderConfigurer;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.instance.datasource.SimpleDataSourceComponent;
import com.datasphere.engine.shaker.processor.instance.defaultcomponent.DefaultComponentInstance;
import com.datasphere.engine.shaker.processor.instance.predatacomponent.PreDataComponentInstance;
import com.datasphere.engine.shaker.processor.instance.validation.base.AbstractComponentValition;
import com.datasphere.engine.shaker.processor.instance.validation.base.IBaseValidation;
import com.datasphere.engine.shaker.processor.instance.validation.instances.*;
import com.datasphere.engine.shaker.processor.service.ComponentInstanceSnapshotService;
import com.datasphere.engine.shaker.processor.service.ComponentService;
import com.datasphere.engine.shaker.processor.service.ProcessInstanceService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.server.manager.module.component.buscommon.constant.ComponentClassification;
import com.datasphere.server.manager.module.component.instance.domain.ComponentInstance;
import com.datasphere.server.manager.module.component.instance.service.ComponentInstanceRelationService;
import com.datasphere.server.manager.module.component.instance.service.ComponentInstanceService;
import com.datasphere.server.manager.module.component.instance.service.PreDataComponentService;
import com.datasphere.server.manager.module.dal.service.DataAccessor;
import com.datasphere.server.manager.module.panel.service.PanelServiceImpl;
import com.datasphere.server.manager.module.panel.service.SubPanelService;
import com.jusfoun.common.springmvc.exception.JIllegalOperationException;

//@org.springframework.stereotype.Component("componentFactory")
public class ComponentFactory {

	@Inject
	private ComponentInstanceService componentInstanceService;
	@Inject
	private ComponentInstanceRelationService componentInstanceRelationService;
	@Inject
	private ProcessRecordService processRecordService;

//	@Inject
//	private ComponentService componentService;
	@Inject
	private ProcessInstanceService processInstanceService;
	@Inject
	private ComponentInstanceSnapshotService componentInstanceSnapshotService;

	@Inject
	private PanelServiceImpl panelService;
//	@Inject
	private DataAccessor dataAccessor;
//	@Inject
	private CustomizedPropertyPlaceholderConfigurer propertiesBean;
	@Inject
	private SubPanelService subPanelService;
	@Inject
	private PreDataComponentService preDataComponentService;
//	@Inject
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
