package com.datasphere.engine.shaker.processor.prep.constant;

import javax.inject.Singleton;

@Singleton
public class GlobalConfig {

    private  static String operate_callback_baseurl;

    private static String operate_algm_compute_url;

    private static String mq_operate_type;

    private static String mq_evaluation_type;


    //@Value("${operate.callback.baseurl}")
    public  void setOperate_callback_baseurl(String operate_callback_baseurl) {
        GlobalConfig.operate_callback_baseurl = operate_callback_baseurl;
    }

    //@Value("${operate.algm.compute.url}")
    public  void setOperate_algm_compute_url(String operate_algm_compute_url) {
        GlobalConfig.operate_algm_compute_url = operate_algm_compute_url;
    }

    //@Value("${mq.operate.type}")
    public  void setMq_operate_type(String mq_operate_type) {
        GlobalConfig.mq_operate_type = mq_operate_type;
    }

    //@Value("${mq.evaluation.type}")
    public  void setMq_evaluation_type(String mq_evaluation_type) {
        GlobalConfig.mq_evaluation_type = mq_evaluation_type;
    }

    public static String OperateCallBackBaseUrl() {
        return operate_callback_baseurl;
    }

    public static String OperateAlgmComputeUrl() {
        return operate_algm_compute_url;
    }

    public static String WSOperateType() {
        return mq_operate_type;
    }

    public static String WSEvaluationType() {
        return mq_evaluation_type;
    }
}
