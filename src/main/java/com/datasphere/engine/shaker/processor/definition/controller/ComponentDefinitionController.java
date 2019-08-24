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

package com.datasphere.engine.shaker.processor.definition.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.definition.ComponentDefinition;
import com.datasphere.engine.shaker.processor.definition.service.ComponentDefinitionServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.reactivex.Single;
import java.util.Map;

public class ComponentDefinitionController extends BaseController {

	@Autowired
	ComponentDefinitionServiceImpl componentDefinitionService;

	public static final String BASE_PATH = "/component/definition";
	
	@RequestMapping(value = BASE_PATH+"/listAll", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listAll() {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentDefinitionService.listAll());
		});
	}
	
	@RequestMapping(value = BASE_PATH+"/listForTree", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listForTree(@Parameter String creator, @Parameter String name) {
		return Single.fromCallable(() -> {
		 	return JsonWrapper.successWrapper(componentDefinitionService.listForTree(creator, name));
		});
	}
	
	@RequestMapping(value = BASE_PATH+"/get", method = RequestMethod.POST) 
	public Single<Map<String,Object>> get(@Parameter String id) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentDefinitionService.get(id));
		});
	}

	/**
	 * 通过ComponentCode获取数据处理组件的ID
	 * @param name
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/getDFIDByName", method = RequestMethod.POST) 
	public Single<Map<String,Object>> getDFIDByName(@Parameter String name) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentDefinitionService.getDFIDByName(name));
		});
	}

	/**1
	 * 获取所有的数据处理组件定义
	 * @param creator
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/listDataProcessComponent", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listDataProcessComponent(@Parameter String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentDefinitionService.listDataProcessComponent(creator));
		});
	}

	@RequestMapping(value = BASE_PATH+"/listBy", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listBy(@Body ComponentDefinition componentDefinition) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentDefinitionService.listBy(componentDefinition));
		});
	}

	@RequestMapping(value = BASE_PATH+"/create", method = RequestMethod.POST) 
	public Single<Map<String,Object>> create(@Body ComponentDefinition componentDefinition) {
		return Single.fromCallable(() -> {
			componentDefinitionService.insert(componentDefinition);
			return JsonWrapper.successWrapper();
		});
	}

	/**
	 * 根据ID删除组件定义
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/delete", method = RequestMethod.POST) 
	public Single<Map<String,Object>> delete(@Parameter String id) {
		return Single.fromCallable(() -> {
			int num = componentDefinitionService.delete(id);
			if (0 == num)
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS,
						ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			return JsonWrapper.successWrapper(num);
		});
	}

	@RequestMapping(value = BASE_PATH+"/update", method = RequestMethod.POST) 
	public Single<Map<String,Object>> update(@Body ComponentDefinition componentDefinition) {
		return Single.fromCallable(() -> {
			int num = componentDefinitionService.update(componentDefinition);
			if (0 == num)
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS,
						ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			return JsonWrapper.successWrapper(num);
		});
	}

	@RequestMapping(value = BASE_PATH+"/exists", method = RequestMethod.POST) 
	public Single<Map<String,Object>> exists(@Parameter String id) {
		return Single.fromCallable(() -> {
			boolean b = componentDefinitionService.exists(id);
			return JsonWrapper.successWrapper(b);
		});
	}
}
