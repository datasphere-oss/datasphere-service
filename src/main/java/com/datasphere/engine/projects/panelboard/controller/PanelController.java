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

package com.datasphere.engine.projects.panelboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datasphere.engine.core.common.BaseController;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.workflow.panelboard.model.Panel;
import com.datasphere.engine.shaker.workflow.panelboard.model.PanelType;
import com.datasphere.engine.shaker.workflow.panelboard.service.PanelServiceImpl;

import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class PanelController extends BaseController {
	@Autowired PanelServiceImpl panelServiceImpl;
//	@Inject  ProjectServiceImpl projectServiceImpl;
	public static final String BASE_PATH = "/panel";

	/**
	 * 删除项目下的所有面板
	 * 传入一个项目id，删除这个项目下的所有面板。先检查面板是否能够删除（面板运行中则不能删除），不能删除则返回原因，且设置一个状态值，
	 * 区别删除过程的其他错误原因。
	 */
	@PostMapping(value = BASE_PATH + "/deletePanelByProjectId")
	public Single<Map<String,Object>> deletePanelByProjectId(@RequestParam String projectId) {
		return Single.fromCallable(() -> {
			int status = panelServiceImpl.deleteById(projectId);
			if (0 == status) {
				return JsonWrapper.successWrapper();
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION,
						ExceptionConst.get(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION));
			}
		});
	}

	/**1
	 * 创建面板
	 */
	@PostMapping(value = BASE_PATH + "/createPanel")
	public Single<Map<String,Object>> createPanel(@RequestBody Panel panel, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			// 校验面板名称是否重复
			boolean flag = panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator());
			if (flag) {
				return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
			}
			Panel panel2 = panelServiceImpl.create(panel, token);
			if (panel2 == null) return JsonWrapper.failureWrapper("创建项目面板失败！");
			return JsonWrapper.successWrapper(panel2);
		});
	}

	/**
	 * 创建面板
	 */
	@PostMapping(value = BASE_PATH + "/createcyl")
	public Single<Map<String,Object>> createcyl(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			List<Panel> list = new ArrayList<>();
			list.add(panelServiceImpl.getPanelById(null));
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 查询最后一次访问的面板
	 */
	@PostMapping(value = BASE_PATH + "/lastAccessed")
	public Single<Map<String,Object>> getLastAccessed(@RequestBody Panel panel1) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.getLast(panel1.getCreator()));
		});
	}

	/**
	 * 查询最后一次访问的面板
	 */
	@PostMapping(value = BASE_PATH + "/lastcyl")
	public Single<Map<String,Object>> getLastcyl(@RequestParam String creator) {
		return Single.fromCallable(() -> {
			List<Panel> list = new ArrayList<>();
			Panel panel1 = panelServiceImpl.getLast(creator);
			if (null != panel1) list.add(panel1);
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 按钮：另存为
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/save")
	public Single<Map<String,Object>> save(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
//			if (!StringUtils.isBlank(panel.getId()) && !StringUtils.isBlank(panel.getPanelName())) {
////				Map<String, String> panelSaveMap = panelServiceImpl.saveAs(id, projectId, panelName, panelDesc);
//				Map<String, String> panelSaveMap = panelServiceImpl.saveAs(panel);
//				if (panelSaveMap != null && panelSaveMap.size() > 0) {
//					return JsonWrapper.successWrapper(panelServiceImpl.panelPageDetail(panelSaveMap.get("projectId"), panelSaveMap.get("id")));
//				} else {
//					return JsonWrapper.failureWrapper("面板另存为失败");
//				}
//			} else {
//				return JsonWrapper.failureWrapper("名称和项目id不为空");
//			}
			if (!StringUtils.isBlank(panel.getPanelName())) {
				if (!panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator())) {
					String id = panelServiceImpl.saveAs(panel).getId();
					return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
				} else {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT,
							ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL,
						ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	@PostMapping(value = BASE_PATH + "/saveAsCyl")
	public Single<Map<String,Object>> saveAsCyl(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panel.getPanelName()) && !StringUtils.isBlank(panel.getProjectId())) {
				if (!panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator())) {
					String id = panelServiceImpl.saveAs(panel).getId();
					Panel panel2 = panelServiceImpl.getPanelById(id);
					List<Panel> list = new ArrayList<>();
					list.add(panel2);
					return JsonWrapper.successWrapper(list);
				} else {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL, ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	/**2
	 * 更新面板
	 */
	@PostMapping(value = BASE_PATH + "/update")
	public Single<Map<String,Object>> update(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panel.getPanelName()) && !StringUtils.isBlank(panel.getCreator())) {
				boolean flag = panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator());
				if (!flag) {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL, ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
			int num = panelServiceImpl.update(panel);
			if (num > 0) {
				return JsonWrapper.successWrapper("Success");
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS, ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			}
		});
	}

	/**
	 * 删除面板
	 * @param id 面板id
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/delete")
	public Single<Map<String,Object>> delete(@RequestParam String id) {
		return Single.fromCallable(() -> {
			int status = panelServiceImpl.delete(id);
			if (status != 2) {
				return JsonWrapper.successWrapper(status);
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION,
						ExceptionConst.get(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION));
			}
		});
	}

	/**
	 * 根据id获得面板信息
	 *
	 * @param id 面板id
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/getPanel")
	public Single<Map<String,Object>> getPanel(@RequestParam String id, @RequestParam String creator) {
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(id) || StringUtils.isBlank(creator)) {
				return JsonWrapper.failureWrapper("参数不能为空");
			}
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
		});
	}

	/**
	 * 模糊查询
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/listByPanel")
	public Single<Map<String,Object>> listByPanel(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.listBy(panel));
		});
	}

	/**
	 * 根据项目id查找该项目下的面板列表，分页，排序功能需要实现
	 *
	 * @param panel 项目id
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/getPanelsByProjectId")
	public Single<Map<String,Object>> getPanelsByProjectId(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelsByProjectIdOrdeyPager(panel));
		});
	}

	/**
	 * 根据多个项目id查找每个项目下的面板列表，分页，排序功能需要实现。返回包含所以项目（每个项目id + 对应的面板信息）
	 * @param panel
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/listByProjectId")
	public Single<Map<String,Object>> listByProjectId(@RequestBody Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.getByProjectIdList(panel));
		});
	}

	/** new0
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板。
	 * 实际是获取面板的流程信息
	 */
	@PostMapping(value = BASE_PATH + "/panelPageDetail")
	public Single<Map<String,Object>> panelPageDetail(@RequestParam String projectId, @RequestParam String panelId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(panelServiceImpl.panelDetail(projectId, panelId, token));
		});
	}

	/**
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板。
	 */
	@PostMapping(value = BASE_PATH + "/panelPageDetail")
	public Single<Map<String,Object>> panelPageDetail(@RequestParam String id, @RequestParam String panelId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(panelServiceImpl.panelPageDetail(id, panelId, token));
		});
	}

	/**
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板
	 */
	@PostMapping(value = BASE_PATH + "/panelPageDetailCyl")
	public Single<Map<String,Object>> panelPageDetailCyl(@RequestParam String id, @RequestParam String panelId) {
		return Single.fromCallable(() -> {
//			PanelWithAll all = panelServiceImpl.panelPageDetail(id, panelId);
//			List<PanelWithAll> list = new ArrayList<>();
//			list.add(all);
			return JsonWrapper.successWrapper(null);
		});
	}

	/**
	 * 数据源被多少个面板引用，以及是那些面板在引用，返回面板数量和面板信息（包括面板id，面板名称 和 项目id）
	 * @param id 组件id
	 * @param creator
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/sourceTrace")
	public Single<Map<String,Object>> sourceTrace(@RequestParam String id, @RequestParam String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.sourceTrace(id, creator));
		});
	}

	/**
	 * 多个数据源中每个数据源被多少个面板引用，以及是那些面板在引用，返回面板数量和面板信息（包括面板id，面板名称 和 项目id）。
	 * @param id
	 * @param creator
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/someSourceTrace")
	public Single<Map<String,Object>> someSourceTrace(@RequestParam String id, @RequestParam String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.someSourceTrace(id, creator));
		});
	}

	/**
	 * 验证名称，不允许重命名
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/veriftyName")
	public Single<Map<String,Object>> veriftyName(@RequestParam String panelName, @RequestParam String projectId, @RequestParam String creator) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panelName) && !StringUtils.isBlank(projectId)) {
				boolean b = panelServiceImpl.verifyName(panelName, projectId, creator);
				if (b) {
					return JsonWrapper.successWrapper();
				}
				return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT,
						ExceptionConst.get(ExceptionConst.NAME_REPEAT));
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL,
						ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	/**
	 * 根据creator获取项目列表
	 * @param panel
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/listAllPanels")
	public Single<Map<String,Object>> listAllPanels(@RequestBody Panel panel,HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.jobFailure(-1, "token不能为空！");
			List<Panel> list = panelServiceImpl.getAllPanelList(panel, token);
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 4.根据id获得项目信息(包含面板列表信息)
	 * @param id 项目id
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/getPanel")
	public Single<Map<String,Object>> getPanel(@RequestParam String id) {
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(id)) {
				return JsonWrapper.failureWrapper("参数不能为空");
			}
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
		});
	}

}
