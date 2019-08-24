package com.datasphere.engine.common.message;

import java.io.IOException;

import com.datasphere.core.common.utils.O;

public class CustomizedPropertyPlaceholderConfigurer {
	private String language = "ZH";

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Object get(Object name) throws IOException {
		String rs = "";
		switch (name.toString()) {
			case "DELETE_SUCCESS":
				rs = "\\u5220\\u9664\\u6210\\u529F!";
				break;
			case "DELETE_PANEL_EXISTS_RUNNING_EXCEPTION":
				rs = "\\u5B58\\u5728\\u6B63\\u5728\\u8FD0\\u884C\\u7684\\u9762\\u677F\\uFF01\\u65E0\\u6CD5\\u5220\\u9664\\uFF01";
				break;
			case "PROCESS_RUN_LONGTIME":
				rs = "\\u8FD0\\u884C\\u65F6\\u95F4\\u592A\\u957F\\uFF01";
				break;
			case "CALCULATE_RESPONSE_ERROR":
				rs = "\\u8BA1\\u7B97\\u5E73\\u53F0\\u8BA1\\u7B97\\u5931\\u8D25!";
				break;
			case "ISSTOP":
				rs = "\\u5DF2\\u7ECF\\u505C\\u6B62";
				break;
		}
		return rs;
	}
}
