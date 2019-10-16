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

package com.datasphere.engine.shaker.processor.definition.constant;

public class GlobalConstant {
	public final static String USER_CONTEXT = "DATASPHERE_USER_CONTEXT";
	
	public final static String DATASOURCE_CSV = "CSV";
	public final static String DATASOURCE_ARFF = "ARFF";

	public final static String DATASOURCE_COLUMNNAME = "columnName";
	public final static String DATASOURCE_COLUMNTYPE = "columnType";
	
	public final static String DATASOURCE_COLUMNKEYWORD = "keyWord";
	public final static String DATASOURCE_COLUMNRULE = "rule";
	
	public final static  String LINE_SEPARATOR = System.getProperty("line.separator");

	public static class COMPONENT_CLASSIFICATION {
		public static String MY_DATASOURCE = "001"; // my datasource
		public static String SUBSCRIBE_DATASOURCE = "001";  // subscribe datasource

		// Preprocessing component
		public static String PREPROCESS = "002";
		// Statistical analysis component
		public static String STATISTICS = "003";
		// Machine learning component
		public static String MACHINE_LEARNING = "004";
	}
	
	// Internal server error. Contains logic error, preset data error.
	public static class INNER_ERROR {
		// Preconfigured component properties lack a name property
		public static Integer MISS_COMPONENT_NAME = 550;
	}
	
	// Other types of errors
	public static class ERROR_CODE {
		// Request parameter JSON parsing error
		public static Integer REQ_JSON_PARSE = 100;
		
		public static Integer JSON_TO_STRING = 101;
		// The preset component name does not match the application's built-in component name
		public static Integer COMPONENT_NAME_CONSISTENCY = 102;
	}
	
	public static class COMPONENT_NODE_TYPE {
		// Classification node
		public static Integer CLASSIFICATION = 1;
		// component node
		public static Integer COMPONENT = 2;
	}
	
	public static class DATASOURCE_DELETE_TYPE {
		// Failed to delete datasource and ComponentDefinition
		public static Integer DEL_TABEL = 100;
		// Failed to delete tables after warehousing
		public static Integer DEL_DATASETTABEL = 102;
		// There is a reference panel, deletion failed
		public static Integer DEL_PANEL = 101;
	}
}
