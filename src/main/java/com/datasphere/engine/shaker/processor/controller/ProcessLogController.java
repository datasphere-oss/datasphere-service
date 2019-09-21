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

package com.datasphere.engine.shaker.processor.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.processor.service.ProcessInstanceService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.server.connections.utils.StringUtils;
import io.reactivex.Single;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProcessLogController extends BaseController {
	private final static Log logger = LogFactory.getLog(ProcessController.class);
	public static final String BASE_PATH = "/workflow/log";

	@Autowired
	private ProcessInstanceService processInstanceService;
	@Autowired
	private ProcessRecordService processRecordService;

	/**
	 * 查询面板最后一次运行记录
	 * @param panelId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = BASE_PATH + "/getPanelLog")
	public Object processInstanceLog(@RequestParam String panelId) {
		return JsonWrapper.successWrapper(processInstanceService.getLastByPanelId(panelId));
	}

	/**0
	 * 查询某一组件最后一次运行情况
	 * @param componentId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = BASE_PATH + "/getComponentLog")
	public Single<Map<String,Object>> componentLog(@RequestParam String componentId) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(componentId)) {
				return JsonWrapper.successWrapper(processRecordService.getLatestRecord(componentId));
			} else {
				return JsonWrapper.failureWrapper("查询"+componentId+"运行log-失败,参数不能为空");
			}
		});
	}

	/**
	 * 获取此面板最后一次运行时组件实例的状态
	 * @param panelId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = BASE_PATH + "/getAllLogByPanelId")
	public Object getAllLogByPanelId(@RequestParam String panelId) {
		return JsonWrapper.successWrapper(processRecordService.getPanelComponentInstancesLatestRecord(panelId));
	}
}
