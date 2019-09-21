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
    public Object check(@RequestParam String processId) {
        return Single.fromCallable(() -> {
            ReturnData result = new ReturnData();
            try {
                if (StringUtils.isNotBlank(processId)) {
                    if (dataProcessService.check(processId) == 0)
                        return result.setCode(DataNotExist);
                } else
                    result.setCode(ParameterInvalid);
            } catch (Exception e) {
                result.setCode(Failed);
            }
            return result;
        });
    }

	@RequestMapping(value = BASE_PATH+"/getDefaultProgramOperates/{processId}", method = RequestMethod.POST) 
    public ReturnData<ProgramFlowData> getDefaultProgramOperates(@RequestParam("processId") String processId) {
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
    public ReturnData join(@RequestBody JoinInputData inputdata) {
        ReturnData result = new ReturnData();
        try {
            JoinOutputData ouputData = dataProcessService.join(inputdata);
            result.setData(ouputData);
        } catch (RuntimeException e) {
            result.setCode(Failed);
        }
        return result;
    }



}
