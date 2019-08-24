package com.datasphere.engine.shaker.processor.instance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JsonWrapper;

import io.reactivex.Single;

/**
 * 组件实例操作接口
 * 组件拖拽进panel、组件从面板中删除、组件在面板中编辑
 */
@Controller
public class ComponentInstanceController extends BaseController {
	public static final String BASE_PATH = "/component/instances";

	@Autowired
	ComponentInstanceService componentInstanceService;

	@Autowired
	DataQueryService dataQueryService;
	/**
	 * 查询组件实例的详细信息
	 */
	@Post(BASE_PATH + "/get")
	public Single<Map<String,Object>> get(@Parameter String id, @Parameter String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentInstanceService.get(id));
		});
	}

	/**
	 * 创建组件实例：组件拖拽进panel
	 * @param componentInstance
	 * @return
	 */
	@Post(BASE_PATH + "/create")
	public Single<Map<String,Object>> create(@Body ComponentInstance componentInstance, HttpRequest request) { //, @Parameter String reserve
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			if (componentInstanceService.insert(componentInstance, token) > 0 ) {
				return JsonWrapper.successWrapper(componentInstanceService.get(componentInstance.getId()));
			} else {
				return JsonWrapper.failureWrapper("创建组件实例：失败");
			}
		});
	}

	/**
	 * 更新组件实例基本信息和属性信息
	 * @param componentInstance
	 * @return
	 */
	@Post(BASE_PATH + "/update")
	public Single<Map<String,Object>> update(@Body ComponentInstance componentInstance) {
		return Single.fromCallable(() -> {
			if (0 == componentInstanceService.update(componentInstance))
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS, ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			return JsonWrapper.successWrapper("1");
		});
	}

	/**
	 * 更新组件实例位置以及相关关联线的位置
	 * @param entity
	 * @return
	 */
	@Post(BASE_PATH + "/updatePosition")
	public Single<Map<String,Object>> updatePosition(@Body UpdatePositionEntity entity) {
		return Single.fromCallable(() -> {
			componentInstanceService.updatePosition(entity);
			return JsonWrapper.successWrapper("1");
		});
	}

	/**
	 * 获得面板全部组件实例
	 * 
	 * @param panelId
	 * @return
	 */
	@Post(BASE_PATH + "/listByPanelId")
	public Single<Map<String,Object>> listBy(@Parameter String panelId) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(componentInstanceService.getAllComponentInstancesWithPanel(panelId));
		});
	}

	/**
	 * 删除组件实例
	 * 操作：删除面板中的一个组件
	 * @param id
	 * @return
	 */
	@Post(BASE_PATH + "/delete")
	public Single<Map<String,Object>> delete(@Parameter String id, HttpRequest request) {//, @Parameter String creator
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			DeleteComponentInstanceResult deleteComponentInstanceResult = componentInstanceService.deleteComponentInstance(id, token);
			if (deleteComponentInstanceResult != null) {
				return JsonWrapper.successWrapper(deleteComponentInstanceResult);
			} else {
				return JsonWrapper.failureWrapper(deleteComponentInstanceResult);
			}
		});
	}

	/**0
	 * 组件实例的复制
	 */
	@Post(BASE_PATH + "/copy")
	public Single<Map<String,Object>> copy(@Parameter String creator,@Parameter String componentInstanceId, HttpRequest request)  {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			if (StringUtils.isBlank(creator) || StringUtils.isBlank(componentInstanceId)) {
				return JsonWrapper.failureWrapper("参数不能为空！");
			}
			return JsonWrapper.successWrapper(componentInstanceService.copy(creator, componentInstanceId));
		});
	}

	/**
	 * 得获组件实例输出的数据集描述
	 * @param componentInstanceId
	 * @param output
	 * @return
	 */
	@Post(BASE_PATH + "/getOutput")
	public Single<Map<String,Object>> getOutput(@Parameter String componentInstanceId,@Parameter String output) {
		return Single.fromCallable(() -> {
			List<Dataset> datasets = componentInstanceService.getDatasetByComponentId(componentInstanceId, output);
			return JsonWrapper.successWrapper(datasets);
		});

	}


	@Post(BASE_PATH + "/dataQuery")
	public Single<Map<String,Object>> dataQuery(@Body QueryDataParams query) {
		return Single.fromCallable(() -> {
			if(!StringUtils.isBlank(query.getComponentInstanceId())){
				//查询实例 sql
				DataSetInstance dataSetInstance = componentInstanceService.getDataSetInfo(query.getComponentInstanceId());
				if(dataSetInstance != null && !StringUtils.isBlank(dataSetInstance.getCiSql())){
					query.setSql(dataSetInstance.getCiSql());
				}else if(query.getSql() == null){
					return JsonWrapper.failureWrapper("查询无数据!");
				}else{
					return JsonWrapper.failureWrapper("请检查寻条件!");
				}

			}
			Map<String, Object> result = dataQueryService.dataQuery(query.getSql());
			if(result != null){
				return JsonWrapper.successWrapper(result);
			}
			return JsonWrapper.failureWrapper("查询失败，请检查数据集!");
		});

	}

	
}
