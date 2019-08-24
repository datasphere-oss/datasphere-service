package com.datasphere.engine.shaker.processor.controller;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.processor.ProcessRunCallable;
import com.datasphere.engine.shaker.processor.buscommon.ReturnMessageUtils;
import com.datasphere.engine.shaker.processor.instance.callbackresult.ComponentCalcuateResult;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.message.status.notice.CallBackStatusMessage;
import com.datasphere.engine.shaker.processor.service.ProcessDaasService;
import com.datasphere.engine.shaker.processor.service.ProcessRecordService;
import com.datasphere.engine.shaker.processor.service.ProcessService;
import com.datasphere.engine.shaker.processor.stop.StopSingleInstance;
import com.datasphere.engine.shaker.workflow.panel.service.PanelServiceImpl;
import com.datasphere.server.connections.utils.StringUtils;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * 数据流-处理流程controller
 * @author jeq
 */
//@Validated
public class ProcessController extends BaseController {
	private final static Log logger = LogFactory.getLog(ProcessController.class);
	public static final String BASE_PATH = "/dmp-dfc/process";

	@Inject
	private PanelServiceImpl panelService;
	@Inject
	private ProcessService processService;
	@Inject
	private ProcessRecordService processRecordService;
	@Inject
	private ComponentInstanceService componentInstanceService;
	@Inject
    private ProcessDaasService processDaasService;

	/** Daas-1 test-ok
	 * 指定组件实例的数据查询
	 * @param componentInstanceId
	 * @return
	 */
	@Post(BASE_PATH + "/queryByCIId")
	public Single<Map<String,Object>> queryByCIId(@Parameter String componentInstanceId, HttpRequest request) {
		return Single.fromCallable(() -> {
			String ciName = "";
			String jobId = "1";
			try {
				if (StringUtils.isBlank(componentInstanceId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				ciName = componentInstanceService.getComponentInstanceById(componentInstanceId).getCiName();
				String token = request.getParameters().get("token");
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
    @Post(BASE_PATH + "/runToThis")
    public Single<Map<String,Object>> runToThis(@Parameter String panelId, @Parameter String componentInstanceId, HttpRequest request) {
        return Single.fromCallable(() -> {
			String panelName = "";
			String jobId = "2";
            try {
            	if (StringUtils.isBlank(panelId) || StringUtils.isBlank(componentInstanceId)) {
					processService.addCILog("",panelId,componentInstanceId,"参数为空",3);
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				panelName = panelService.getPanelById(panelId).getPanelName();
				String token = request.getParameters().get("token");
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
	@Post(BASE_PATH + "/runFromThis")
	public Single<Map<String,Object>> runFromThis(@Parameter String panelId, @Parameter String componentInstanceId, HttpRequest request) {
		return Single.fromCallable(() -> {
			try {
				if (StringUtils.isBlank(panelId) || StringUtils.isBlank(componentInstanceId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				}
				String token = request.getParameters().get("token");
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
	@Post(BASE_PATH + "/runAll")
	public Single<Map<String,Object>> runAll(@Parameter String panelId, HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
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
	@Post(BASE_PATH + "/cancel")
	public Single<Map<String,Object>> cancel(@Parameter String jobId, HttpRequest request) {
		return Single.fromCallable(() -> {
			try {
				if (StringUtils.isBlank(jobId)) {
					return JsonWrapper.failureWrapper(ReturnMessageUtils.ParameterIsNull);
				} else {
					String token = request.getParameters().get("token");
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
	@Post(BASE_PATH + "/getProcessState")
	public Single<Map<String,Object>> getProcessState(@Parameter String jobId) {
		return Single.fromCallable(() -> {
			try {
//				String sql = processService.getProcessSQL(panelId,1);
//				String jobId = processService.getJobId(sql);
				return JSON.parseObject(processDaasService.getJobState(jobId));
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
	@Post(BASE_PATH + "/run")
	public Map<String,Object> run(@Parameter String panelId) {
//	public Single<Map<String,Object>> run(@Parameter String panelId) { //, String creator
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
	@Post(BASE_PATH + "/stop")
	public Single<Map<String,Object>> stop(@Parameter String panelId) { //,@Parameter String creator
		return Single.fromCallable(() -> {
			try {
				StopSingleInstance.getInstances().stop(panelId);
				return JsonWrapper.successWrapper(panelId);
			} catch (Exception e) {
				return JsonWrapper.failureWrapper(panelId);
			}
		});
	}

	@Post(BASE_PATH + "/run_to")
	public Single<Map<String,Object>> runTo(@Parameter String panelId, @Parameter String componentId) {//, String creator
//		return processHandle(new ProcessRunCallable() {
//			@Override
//			public String call(){
//				StopSingleInstance.getInstances().start(panelId);
//				return processService.runProcessTo(exchangeSSOService.getAccount(token), panelId, componentId);
//			}
//		});
		return null;
	}

	@Post(BASE_PATH + "/run_from")
	public Single<Map<String,Object>> runFrom(@Parameter String panelId, @Parameter String componentId) {//, String creator
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
	@Post(BASE_PATH + "/callBack/{id}")
	public void callBackUrl(@Parameter String id, @Parameter int code, @Parameter String message) { //, String creator
		ComponentCalcuateResult calcuateResult = new ComponentCalcuateResult();
		calcuateResult.setStatus(0 == code ? ComponentInstanceStatus.SUCCESS : ComponentInstanceStatus.FAILURE);
		calcuateResult.setMessage(message);
		CallBackStatusMessage.getInstance().add(id, calcuateResult);
		logger.info("计算平台回调结果===>code=" + code + ",message=" + message);
	}

	@Post(BASE_PATH + "/lastRecord")
	public Single<Map<String,Object>> lastRecord(@Parameter String componentId) { //, String creator
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
