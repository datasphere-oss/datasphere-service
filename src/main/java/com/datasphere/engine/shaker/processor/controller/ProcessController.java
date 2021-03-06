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

import com.alibaba.fastjson.JSON;
import com.datasphere.engine.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.datasource.connections.utils.StringUtils;
import com.datasphere.engine.shaker.processor.ProcessRunCallable;
import com.datasphere.engine.shaker.processor.instance.callbackresult.ComponentCalcuateResult;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.message.status.notice.CallBackStatusMessage;
import com.datasphere.engine.shaker.processor.service.ProcessDSSService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.engine.shaker.processor.service.ProcessService;
import com.datasphere.engine.shaker.processor.stop.StopSingleInstance;
import com.datasphere.engine.shaker.processor.utils.ReturnMessageUtils;
import com.datasphere.engine.shaker.workflow.panelboard.service.PanelServiceImpl;

import io.reactivex.Single;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * dataflow controller
 */
@Controller
public class ProcessController extends BaseController {
	private final static Log logger = LogFactory.getLog(ProcessController.class);
	public static final String BASE_PATH = "/workflow";

	@Autowired
	private PanelServiceImpl panelService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private ProcessRecordService processRecordService;
	@Autowired
	private ComponentInstanceService componentInstanceService;
	@Autowired
    private ProcessDSSService processDSSService;

	/** Daas-1 test-ok
	 * 指定组件实例的数据查询
	 * @param componentInstanceId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/queryByCIId")
	public Single<Map<String,Object>> queryByCIId(@RequestParam String componentInstanceId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String ciName = "";
			String jobId = "1";
			try {
				if (StringUtils.isBlank(componentInstanceId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				ciName = componentInstanceService.getComponentInstanceById(componentInstanceId).getCiName();
				String token = request.getParameter("token");
				if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
				Map<String, Object> rs = processService.queryByCIId(componentInstanceId, token);
				if (rs != null) {
					if (rs.size() == 3) {
						return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"组件["+ciName + "]"+ rs.get("message"));
					}
					return JsonWrapper.successWrapper(rs);//0
				}
				return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"组件["+ciName +"]查询失败！(建议联系管理员)" );
			} catch (Exception e) {
				processService.addCILog("","",componentInstanceId,"查询失败！异常："+e.getMessage(),2);
				return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"组件["+ciName +"]查询失败！" );
			}
		});
	}

    /** Daas-2.1
     * 运行到此
     * @param panelId
     * @param componentInstanceId
     * @return
     */
	@PostMapping(value = BASE_PATH + "/runToThis")
    public Single<Map<String,Object>> runToThis(@RequestParam String panelId, @RequestParam String componentInstanceId, HttpServletRequest request) {
        return Single.fromCallable(() -> {
			String panelName = "";
			String jobId = "2";
            try {
            	if (StringUtils.isBlank(panelId) || StringUtils.isBlank(componentInstanceId)) {
					processService.addCILog("",panelId,componentInstanceId,"参数为空",3);
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				panelName = panelService.getPanelById(panelId).getPanelName();
				String token = request.getParameter("token");
				if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
				Map<String, Object> rs = processService.runToByCIId(panelId, componentInstanceId, token);
				if (Integer.parseInt(rs.get("code").toString()) == JsonWrapper.DEFAULT_FAILURE_CODE) {//1
					return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"["+panelName + "]"+ rs.get("message"));
				}
				return JsonWrapper.jobSuccess(jobId, rs.get("message").toString());//0
            } catch (Exception e) {
				processService.addCILog("",panelId,componentInstanceId,"运行失败！异常："+e.getMessage(),2);
				return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"["+panelName +"]运行失败！" );
            }
        });
    }

	/** Daas-2.2 待开发
	 * @TODO 从此运行
	 * @param panelId
	 * @param componentInstanceId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/runFromThis")
	public Single<Map<String,Object>> runFromThis(@RequestParam String panelId, @RequestParam String componentInstanceId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			try {
				if (StringUtils.isBlank(panelId) || StringUtils.isBlank(componentInstanceId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				String token = request.getParameter("token");
				if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
				return JsonWrapper.successWrapper(processService.runFromByCIId(panelId, componentInstanceId,token));
			} catch (Exception e) {
				return JsonWrapper.failureWrapper(panelId + ReturnMessageUtils.ProcessRunFaild);
			}
		});
	}

	/** Daas-3
	 * 全部运行：运行指定项目面板的全部工作流
	 * @param panelId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/runAll")
	public Single<Map<String,Object>> runAll(@RequestParam String panelId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameter("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			String panelName = "";
			String jobId = "3";
			try {
				if (StringUtils.isBlank(panelId)) {
					return JsonWrapper.failureWrapper("["+panelName + "]"+ ReturnMessageUtils.ParameterIsNull);
				}
				panelName = panelService.getPanelById(panelId).getPanelName();
				// 校验是否是合法的工作流模型
				Map<String, Object> result = processService.checkIsOrLegal(panelId);
				if (!"0".equals(result.get("code"))) {
					return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"["+panelName + "]#"+ result.get("message"));
				}
				Map<String, Object> result2 = processService.runAll(panelId,token);
				if (Integer.parseInt(result2.get("code").toString()) != JsonWrapper.DEFAULT_SUCCESS_CODE) {//1
					return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"["+panelName + "]#"+ result2.get("message"));
				}
				return JsonWrapper.jobSuccess(jobId, result2.get("message").toString());//0
			} catch (Exception e) {
				String tip;
				if (e instanceof NullPointerException) {
					tip = "空指针异常！";
				} else {
					tip = e.getMessage();
				}
				processService.addCILog("",panelId,"","运行失败！异常："+e.getMessage(),2);
				componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.FAILURE);
				return JsonWrapper.jobFailure(JsonWrapper.DEFAULT_FAILURE_CODE,"["+panelName +"]运行失败，" + tip);
			}
		});
	}

	/** Daas-4
	 * 暂停任务=取消job
	 * @param jobId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/cancel")
	public Single<Map<String,Object>> cancel(@RequestParam String jobId, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			try {
				if (StringUtils.isBlank(jobId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				} else {
					String token = request.getParameter("token");
					if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
					return JsonWrapper.successWrapper(processService.cancelJobById(jobId, token));
				}
			} catch (Exception e) {
				return JsonWrapper.failureWrapper(jobId + ReturnMessageUtils.CancelJobFaild);
			}
		});
	}

/** 以上接口均提供给(reource.manager+DAAS之后)工作流运行 */



	/**
	 * 查询工作流状态
	 * @param jobId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/getProcessState")
	public Single<Map<String,Object>> getProcessState(@RequestParam String jobId) {
		return Single.fromCallable(() -> {
			try {
//				String sql = processService.getProcessSQL(panelId,1);
//				String jobId = processService.getJobId(sql);
				return JSON.parseObject(processDSSService.getJobState(jobId));
			} catch (Exception e) {
				return JsonWrapper.failureWrapper(jobId);
			}
		});
	}

	/**1
	 * 运行工作流
	 * @param panelId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/run")
	public Map<String,Object> run(@RequestParam String panelId) {
//	public Single<Map<String,Object>> run(@RequestParam String panelId) { //, String creator
//		return Single.fromCallable(() -> {
			return processHandle(() -> {
                // stopService.start(panelId);
                StopSingleInstance.getInstances().start(panelId);// 将panel_id缓存到cache
                return processService.runProcess(null, panelId);
            });
//		});
	}

	/**
	 * 停止工作流
	 * @param panelId
	 * @return
	 */
	@PostMapping(value = BASE_PATH + "/stop")
	public Single<Map<String,Object>> stop(@RequestParam String panelId) { //,@RequestParam String creator
		return Single.fromCallable(() -> {
			try {
				StopSingleInstance.getInstances().stop(panelId);
				return JsonWrapper.successWrapper(panelId);
			} catch (Exception e) {
				return JsonWrapper.failureWrapper(panelId);
			}
		});
	}

	@PostMapping(value = BASE_PATH + "/run_to")
	public Single<Map<String,Object>> runTo(@RequestParam String panelId, @RequestParam String componentId) {//, String creator
//		return processHandle(new ProcessRunCallable() {
//			@Override
//			public String call(){
//				StopSingleInstance.getInstances().start(panelId);
//				return processService.runProcessTo(exchangeSSOService.getAccount(token), panelId, componentId);
//			}
//		});
		return null;
	}

	@PostMapping(value = BASE_PATH + "/run_from")
	public Single<Map<String,Object>> runFrom(@RequestParam String panelId, @RequestParam String componentId) {//, String creator
//		return processHandle(new ProcessRunCallable() {
//			@Override
//			public String call(){
//				StopSingleInstance.getInstances().start(panelId);
//				return processService.runProcessFrom(exchangeSSOService.getAccount(token), panelId, componentId);
//			}
//		});
		return null;
	}




	/**
	 * 模型计算回调地址
	 * @param id
	 * @param code
	 * @param message
	 */
	@PostMapping(value = BASE_PATH + "/callBack/{id}")
	public void callBackUrl(@RequestParam String id, @RequestParam int code, @RequestParam String message) { //, String creator
		ComponentCalcuateResult calcuateResult = new ComponentCalcuateResult();
		calcuateResult.setStatus(0 == code ? ComponentInstanceStatus.SUCCESS : ComponentInstanceStatus.FAILURE);
		calcuateResult.setMessage(message);
		CallBackStatusMessage.getInstance().add(id, calcuateResult);
		logger.info("计算平台回调结果===>code=" + code + ",message=" + message);
	}

	@PostMapping(value = BASE_PATH + "lastRecord")
	public Single<Map<String,Object>> lastRecord(@RequestParam String componentId) { //, String creator
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(processRecordService.getLatestRecord(componentId));
		});
	}

	private Map<String, Object>  processHandle(ProcessRunCallable callable) {
		try {
			String processId = callable.call();
			return JsonWrapper.successWrapper(processId);
		} catch (Exception e) {
			return JsonWrapper.failureWrapper(e.getMessage());
		}
	}
//	private interface ProcessRunCallable {
//		String call() throws Exception;
//	}

}
