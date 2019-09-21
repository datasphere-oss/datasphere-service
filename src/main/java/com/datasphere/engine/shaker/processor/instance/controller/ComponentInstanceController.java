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

package com.datasphere.engine.shaker.processor.instance.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datasphere.common.data.Dataset;
import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.service.DataQueryService;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.DeleteComponentInstanceResult;
import com.datasphere.engine.shaker.processor.instance.model.QueryDataParams;
import com.datasphere.engine.shaker.processor.instance.model.UpdatePositionEntity;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.server.connections.model.DataSetInstance;

import io.reactivex.Single;
import javax.inject.Inject;


/**
 * Component instance operation interface
 * The component is dragged into the panel, the component is removed from the panel, and the component is edited in the panel.
 */
@Controller
public class ComponentInstanceController extends BaseController {
	public static final String BASE_PATH = "/component/instances";

	@Autowired
	ComponentInstanceService componentInstanceService;

	@Autowired
	DataQueryService dataQueryService;
	/**
	 * Query component instance details
	 */
	@RequestMapping(value = BASE_PATH+"/get", method = RequestMethod.POST) 
	public Single<Map<String,Object>> get(@RequestParam String id, @RequestParam String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentInstanceService.get(id));
		});
	}

	/**
	 * Create a component instance: the component drags into the panel
	 * @param componentInstance
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/create", method = RequestMethod.POST) 
	public Single<Map<String,Object>> create(@RequestBody ComponentInstance componentInstance, HttpServletRequest request) { //, @RequestParam String reserve
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("The token cannot be empty!");
			if (componentInstanceService.insert(componentInstance, token) > 0 ) {
				return JsonWrapper.successWrapper(componentInstanceService.get(componentInstance.getId()));
			} else {
				return JsonWrapper.failureWrapper("Create component instance: failed");
			}
		});
	}

	/**
	 * 更新组件实例基本信息和属性信息
	 * @param componentInstance
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/update", method = RequestMethod.POST) 
	public Single<Map<String,Object>> update(@RequestBody ComponentInstance componentInstance) {
		return Single.fromCallable(() -> {
			if (0 == componentInstanceService.update(componentInstance))
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS, ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			return JsonWrapper.successWrapper("1");
		});
	}

	/**
	 * Update the location of the component instance and the location of the associated line
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/updatePosition", method = RequestMethod.POST) 
	public Single<Map<String,Object>> updatePosition(@RequestBody UpdatePositionEntity entity) {
		return Single.fromCallable(() -> {
			componentInstanceService.updatePosition(entity);
			return JsonWrapper.successWrapper("1");
		});
	}

	/**
	 * Get all component instances of the panel
	 * 
	 * @param panelId
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/listByPanelId", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listBy(@RequestParam String panelId) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentInstanceService.getAllComponentInstancesWithPanel(panelId));
		});
	}

	/**
	 * Delete component instance
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/delete", method = RequestMethod.POST) 
	public Single<Map<String,Object>> delete(@RequestParam String id, HttpServletRequest request) {//, @RequestParam String creator
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("The token cannot be empty!");
			DeleteComponentInstanceResult deleteComponentInstanceResult = componentInstanceService.deleteComponentInstance(id, token);
			if (deleteComponentInstanceResult != null) {
				return JsonWrapper.successWrapper(deleteComponentInstanceResult);
			} else {
				return JsonWrapper.failureWrapper(deleteComponentInstanceResult);
			}
		});
	}

	/**
	 * Replication of component instances
	 */
	@RequestMapping(value = BASE_PATH+"/copy", method = RequestMethod.POST) 
	public Single<Map<String,Object>> copy(@RequestParam String creator,@RequestParam String componentInstanceId, HttpServletRequest request)  {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("The token cannot be empty!");
			if (StringUtils.isBlank(creator) || StringUtils.isBlank(componentInstanceId)) {
				return JsonWrapper.failureWrapper("The parameter cannot be empty!");
			}
			return JsonWrapper.successWrapper(componentInstanceService.copy(creator, componentInstanceId));
		});
	}

	/**
	 * Get the data set description of the component instance output
	 * @param componentInstanceId
	 * @param output
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/getOutput", method = RequestMethod.POST) 
	public Single<Map<String,Object>> getOutput(@RequestParam String componentInstanceId,@RequestParam String output) {
		return Single.fromCallable(() -> {
			List<Dataset> datasets = componentInstanceService.getDatasetByComponentId(componentInstanceId, output);
			return JsonWrapper.successWrapper(datasets);
		});

	}


	@RequestMapping(value = BASE_PATH+"/dataQuery", method = RequestMethod.POST) 
	public Single<Map<String,Object>> dataQuery(@RequestBody QueryDataParams query) {
		return Single.fromCallable(() -> {
			if(!StringUtils.isBlank(query.getComponentInstanceId())){
				DataSetInstance dataSetInstance = componentInstanceService.getDataSetInfo(query.getComponentInstanceId());
				if(dataSetInstance != null && !StringUtils.isBlank(dataSetInstance.getCiSql())){
					query.setSql(dataSetInstance.getCiSql());
				}else if(query.getSql() == null){
					return JsonWrapper.failureWrapper("No data!");
				}else{
					return JsonWrapper.failureWrapper("Please check the conditions!");
				}

			}
			Map<String, Object> result = dataQueryService.dataQuery(query.getSql());
			if(result != null){
				return JsonWrapper.successWrapper(result);
			}
			return JsonWrapper.failureWrapper("The query failed, please check the data set!");
		});

	}

	
}
