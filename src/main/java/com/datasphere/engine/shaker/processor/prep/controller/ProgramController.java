package com.datasphere.engine.shaker.processor.prep.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.shaker.processor.prep.ReturnData;
import com.datasphere.engine.shaker.processor.prep.data.ProgramOutputData;
import com.datasphere.engine.shaker.processor.prep.service.ProgramService;

import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.datasphere.engine.shaker.processor.prep.constant.ReturnConst.Failed;
import static com.datasphere.engine.shaker.processor.prep.constant.ReturnConst.ParameterInvalid;

import javax.inject.Inject;

public class ProgramController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);
    public static final String BASE_PATH = "/program";

    @Inject
    private ProgramService programService;

    /**
     * 获取指定方案信息
//     * @param programId 方案ID
     * @return
     */
//    @RequestMapping(value = "info")
//    @ResponseBody
//    public ReturnData info(Integer programId) {
//        ReturnData result = new ReturnData();
//        try {
//            if (null != programId && programId > 0) {
//                ProgramData data = programService.info(programId);
//                if (null != data) result.setData(data);
//                else result.setCode(DataNotExist);
//            } else
//                result.setCode(ParameterInvalid);
//        } catch (PrePscException e) {
//            logger.info(e.getMessage());
//            result.setCode(Failed);
//        }
//        return result;
//    }

    /**
     * 数据处理组件流程：获取默认处理流程信息
     * @param processId 流程ID
     * @return
     */
    @RequestMapping(value = BASE_PATH + "/getDefault", method = RequestMethod.POST)
    public Object getDefault(@RequestParam String processId) {
        return Single.fromCallable(() -> {
            ReturnData result = new ReturnData();
            try {
                if (StringUtils.isNotBlank(processId)) {
                    ProgramOutputData data = programService.getDefaultProgram(processId);
                    if (null != data) result.setData(data);
                    //else result.setCode(DataNotExist);
                } else
                    result.setCode(ParameterInvalid);
            } catch (RuntimeException e) {
                logger.info(e.getMessage());
                result.setCode(Failed);
            }
            return result;
        });
    }

    /**
     * 删除指定方案信息
     *
     * @param programId 方案ID
     * @return
     */
//    @RequestMapping(value = "delete")
//    @ResponseBody
//    public ReturnData delete(Integer programId) {
//        ReturnData result = new ReturnData();
//        try {
//            if (null != programId && programId > 0) {
//                int res = programService.delete(programId);
//                if (res == 0) result.setCode(DataNotExist);
//            } else
//                result.setCode(ParameterInvalid);
//        } catch (PrePscException e) {
//            logger.info(e.getMessage());
//            result.setCode(Failed);
//        }
//        return result;
//    }

    /**
     * 重命名指定方案信息
     *
     * @param programId 方案ID
     * @param newName   方案新名称
     * @return
     */
//    @RequestMapping(value = "rename")
//    @ResponseBody
//    public ReturnData rename(Integer programId, String newName) {
//        ReturnData result = new ReturnData();
//        try {
//            if (null != programId && programId > 0 && StringUtils.isNotBlank(newName)) {
//                int res = programService.rename(programId, newName);
//                if (res == 0) result.setCode(DataNotExist);
//            } else
//                result.setCode(ParameterInvalid);
//        } catch (PrePscException e) {
//            logger.info(e.getMessage());
//            result.setCode(Failed);
//        }
//        return result;
//    }

    /**
     * 另存为新方案
     *
     * @param programId 方案ID
     * @return
     */
//    @RequestMapping(value = "copy")
//    @ResponseBody
//    public ReturnData copy(Integer programId) {
//        ReturnData result = new ReturnData();
//        try {
//            if (null != programId && programId > 0) {
//                ProgramData programData = programService.copy(programId);
//                if (null == programData) result.setCode(null);
//                else result.setData(programData);
//            } else
//                result.setCode(ParameterInvalid);
//        } catch (PrePscException e) {
//            logger.info(e.getMessage());
//            result.setCode(Failed);
//        }
//        return result;
//    }

    /**
     * 设置为默认方案
     *
     * @param programId 方案ID
     * @return
     */
//    @RequestMapping(value = "setDefault")
//    @ResponseBody
//    public ReturnData setDefault(Integer programId,String processId) {
//        ReturnData result = new ReturnData();
//        try {
//            if (null != programId && programId > 0 && StringUtils.isNotBlank(processId)) {
//                int res = programService.setDefault(programId,processId);
//                if (res == 0) result.setCode(DataNotExist);
//            } else
//                result.setCode(ParameterInvalid);
//        } catch (PrePscException e) {
//            logger.info(e.getMessage());
//            result.setCode(Failed);
//        }
//        return result;
//    }

}
