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

package com.datasphere.engine.shaker.processor.buscommon.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class ColumnDefinitionUtil {
	
	private static final ObjectMapper MAPPER=new ObjectMapper();
	
	public static List<Map<String,String>> assembleComponentDefinitionsByColumnDefsStr(String columnDefinitonsStr) throws Exception{
		JsonNode array=MAPPER.readTree(columnDefinitonsStr);
		return assembleComponentDefinitionsByColumnDefs(array);
	}
	
	public static List<Map<String,String>> assembleComponentDefinitionsByColumnDefs(JsonNode array) throws Exception{
		List<Map<String,String>> componentDefinitions=new ArrayList<>();
		for(JsonNode columnDefinition:array){
			Map<String,String> map=new HashMap<>();
			Iterator<String> it=columnDefinition.getFieldNames();
			while(it.hasNext()){
				String key=it.next();
				map.put(key,columnDefinition.path(key).asText());
			}
			componentDefinitions.add(map);
		}
		return componentDefinitions;
	}

}
