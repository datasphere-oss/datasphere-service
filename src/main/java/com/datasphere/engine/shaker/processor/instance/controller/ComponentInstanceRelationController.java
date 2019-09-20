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

import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;

import io.reactivex.Single;

/**
 * 关联线操作接口
 */
@Service
public class ComponentInstanceRelationController extends BaseController {
	public static final String BASE_PATH = "/component/instance/relations";

	@Autowired
	ComponentInstanceService ciService;
	@Autowired
    ComponentInstanceRelationService cirService;
//	@Inject
//	ExchangeSSOService exchangeSSOService;

	/**
	 * 创建关联线
	 * @param componentInstanceRelation
	 */
	@RequestMapping(value = BASE_PATH+"/create", method = RequestMethod.POST) 
	public Single<Map<String,Object>> create(@Body ComponentInstanceRelation componentInstanceRelation, HttpRequest request) { //, @Parameter String creator
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			String message = cirService.insert(componentInstanceRelation, exchangeSSOService.getAccount(token));
			if (!message.equals("success")) return JsonWrapper.failureWrapper(1, message);
			componentInstanceRelation.setSourceDataKey(ciService.getComponentOutDataSetKey(componentInstanceRelation.getSourceComponentInstanceId()).get(componentInstanceRelation.getSourceOutputName()));
			return JsonWrapper.successWrapper(componentInstanceRelation);
		});
	}

	/**
	 * 修改关联线
	 * @param componentInstanceRelation
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/update", method = RequestMethod.POST) 
	public Single<Map<String,Object>> update(@Body ComponentInstanceRelation componentInstanceRelation, HttpRequest request) {//, @Parameter String creator
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(cirService.update(componentInstanceRelation, token));
		});
	}

	/**
	 * 删除关联线
	 */
	@RequestMapping(value = BASE_PATH+"/delete", method = RequestMethod.POST) 
	public Single<Map<String,Object>> delete(@Parameter String id, HttpRequest request) {//,@Parameter String creator
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(cirService.delete(id, token));
		});
	}

	/**
	 * 查询ByField
	 * @param componentInstanceRelation
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/listBy", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listBy(@Body ComponentInstanceRelation componentInstanceRelation) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(cirService.listBy(componentInstanceRelation));
		});
	}
}
