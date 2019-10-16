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

package com.datasphere.engine.shaker.processor.instance.service;

import com.alibaba.fastjson.JSONObject;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.shaker.processor.buscommon.utils.HttpUtils;
import com.datasphere.engine.shaker.processor.prep.ReturnData;
import com.datasphere.engine.shaker.workflow.panelboard.model.sub.PreDataProcessEntity;
import com.datasphere.server.common.exception.JIllegalOperationException;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PreDataComponentService extends BaseService {

	public PreDataProcessEntity getPreDataProcessEntity(String id, String creator) throws Exception {
		int status = check(id);
		if (0 != status) {
			return null;
		}
		String url = this.getDetailUrl() + id;
		String res = HttpUtils.get(url);
		if (StringUtils.isBlank(res)) {
			throw new JIllegalOperationException("Request failed!url=" + url);
		}
		PreDataProcessEntity preDataProcessEntity = null;
		ReturnData result = JSONObject.parseObject(res, ReturnData.class);
		if (result.getCode() == ExceptionConst.Success) {
			String strData = String.valueOf(result.getData());
			preDataProcessEntity = JSONObject.parseObject(strData, PreDataProcessEntity.class);
			preDataProcessEntity.setCreator(creator);
		}
		return preDataProcessEntity;
	}

	public int check(String id) throws Exception {
		String checkUrl = String.valueOf("http://127.0.0.1:8082/drmp/prepsc/process/check/") + "?processId="+id;
		String checkResponse = HttpUtils.get(checkUrl);
		if (!StringUtils.isEmpty(checkResponse)) {
			JSONObject obj = JSONObject.parseObject(checkResponse);
			return obj.getIntValue("code");
		}
		return -1;
	}

	public String getDetailUrl() throws IOException {
		return String.valueOf("http://127.0.0.1:8082/drmp/process/getDefaultProgramOperates/");
	}

}
