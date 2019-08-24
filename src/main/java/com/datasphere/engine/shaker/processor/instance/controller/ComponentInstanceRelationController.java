package com.datasphere.engine.shaker.processor.instance.controller;

import java.util.Map;

import javax.inject.Inject;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstanceRelation;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceRelationService;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceService;
import com.datasphere.resource.manager.module.datasource.service.ExchangeSSOService;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
//import io.micronaut.validation.Validated;
import io.reactivex.Single;

/**
 * 关联线操作接口
 */
//@Validated
public class ComponentInstanceRelationController extends BaseController {
	public static final String BASE_PATH = "/component/instance/relations";

	@Inject
	ComponentInstanceService ciService;
	@Inject
    ComponentInstanceRelationService cirService;
	@Inject
	ExchangeSSOService exchangeSSOService;

	/**
	 * 创建关联线
	 * @param componentInstanceRelation
	 */
	@Post(BASE_PATH + "/create")
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
	@Post(BASE_PATH + "/update")
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
	@Post(BASE_PATH + "/delete")
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
	@Post(BASE_PATH + "/listBy")
	public Single<Map<String,Object>> listBy(@Body ComponentInstanceRelation componentInstanceRelation) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(cirService.listBy(componentInstanceRelation));
		});
	}
}
