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

package com.datasphere.engine.core.common.utils;

public class ConfigUtil {
	String emailRegistSuburl;
	String emailForgetpasswordSuburl;
	String emailChangeSuburl;
	String picturenumSuburl;
	String webulr;
	String uploadFilePath;
	String headIconUrl;
	
	public String getHeadIconUrl() {
		return headIconUrl;
	}
	public void setHeadIconUrl(String headIconUrl) {
		this.headIconUrl = headIconUrl;
	}
	public ConfigUtil(String emailRegistSuburl, String emailForgetpasswordSuburl, String picturenumSuburl,
			String webulr,String uploadFilePath,String emailChangeSuburl,String headIconUrl) {
		this.emailRegistSuburl = emailRegistSuburl;
		this.emailForgetpasswordSuburl = emailForgetpasswordSuburl;
		this.picturenumSuburl = picturenumSuburl;
		this.webulr = webulr;
		this.uploadFilePath=uploadFilePath;
		this.emailChangeSuburl=emailChangeSuburl;
		this.headIconUrl=headIconUrl;
	}
	public String getEmailRegistSuburl() {
		return emailRegistSuburl;
	}
	public void setEmailRegistSuburl(String emailRegistSuburl) {
		this.emailRegistSuburl = emailRegistSuburl;
	}
	public String getEmailForgetpasswordSuburl() {
		return emailForgetpasswordSuburl;
	}
	public void setEmailForgetpasswordSuburl(String emailForgetpasswordSuburl) {
		this.emailForgetpasswordSuburl = emailForgetpasswordSuburl;
	}
	public String getPicturenumSuburl() {
		return picturenumSuburl;
	}
	public void setPicturenumSuburl(String picturenumSuburl) {
		this.picturenumSuburl = picturenumSuburl;
	}
	public String getWebulr() {
		return webulr;
	}
	public void setWebulr(String webulr) {
		this.webulr = webulr;
	}
	public String getUploadFilePath() {
		return uploadFilePath;
	}
	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}
	public String getEmailChangeSuburl() {
		return emailChangeSuburl;
	}
	public void setEmailChangeSuburl(String emailChangeSuburl) {
		this.emailChangeSuburl = emailChangeSuburl;
	}
	
}
