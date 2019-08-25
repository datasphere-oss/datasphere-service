package com.datasphere.engine.shaker.processor.prep.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.shaker.processor.prep.ReturnData;
import com.datasphere.engine.shaker.processor.prep.data.JoinInputData;
import com.datasphere.engine.shaker.processor.prep.data.JoinOutputData;
import com.datasphere.engine.shaker.processor.prep.data.ProgramFlowData;
import com.datasphere.engine.shaker.processor.prep.service.DataProcessService;
import com.datasphere.engine.shaker.processor.prep.service.ProgramService;

import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.datasphere.engine.shaker.processor.prep.constant.ReturnConst.DataNotExist;
import static com.datasphere.engine.shaker.processor.prep.constant.ReturnConst.Failed;
import static com.datasphere.engine.shaker.processor.prep.constant.ReturnConst.ParameterInvalid;

/**
 * data preprocessing by workflow
 */
public class DataProcessController extends BaseController{
    private final static Log logger = LogFactory.getLog(DataProcessController.class);
    public static final String BASE_PATH = "/prepsc/process";

    @Autowired
    private DataProcessService dataProcessService;
    @Autowired
    ProgramService programService;

	@RequestMapping(value = BASE_PATH+"/check", method = RequestMethod.POST) 
    public Object check(@Parameter String processId) {
        return Single.fromCallable(() -> {
            ReturnData result = new ReturnData();
            try {
                if (StringUtils.isNotBlank(processId)) {
                    if (dataProcessService.check(processId) == 0)
                        return result.setCode(DataNotExist);
                } else
                    result.setCode(ParameterInvalid);
            } catch (Exception e) {
                logger.info("获取信息 出错");
                result.setCode(Failed);
            }
            return result;
        });
    }

	@RequestMapping(value = BASE_PATH+"/getDefaultProgramOperates/{processId}", method = RequestMethod.POST) 
    public ReturnData<ProgramFlowData> getDefaultProgramOperates(@Parameter("processId") String processId) {
        ReturnData result = new ReturnData();
        try {

            if (StringUtils.isBlank(processId)) {
                result.setCode(ParameterInvalid);
                return result;
            }

            ProgramFlowData programFlowData = programService.buildFlowData(processId);
            if (null == programFlowData) {
                result.setCode(DataNotExist);
                return result;
            }
            result.setData(programFlowData);

        } catch (Exception e) {
            logger.info("设置为默认方案 出错");
            result.setCode(Failed);
        }
        return result;
    }

    /**1
     * 合并表
     * @param inputdata
     * @return
     */
	@RequestMapping(value = BASE_PATH+"/join", method = RequestMethod.POST) 
    public ReturnData join(@Body JoinInputData inputdata) {
        ReturnData result = new ReturnData();
        try {
            JoinOutputData ouputData = dataProcessService.join(inputdata);
            result.setData(ouputData);
        } catch (RuntimeException e) {
            logger.info("删除指定数据处理出错");
            result.setCode(Failed);
        }
        return result;
    }



}
