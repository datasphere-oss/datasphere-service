package com.datasphere.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datasphere.common.data.JoinPoint;
import com.datasphere.core.common.utils.JSONUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;


public class ParamsVO implements Serializable{
	
	private String code;
	private Map<String,Object> algmAttrs;
	private List<JoinPoint> inputs;
	private List<JoinPoint> outputs;
	private String callbackUrl;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, Object> getAlgmAttrs() {
		return algmAttrs;
	}
	public void setAlgmAttrs(Map<String, Object> algmAttrs) {
		this.algmAttrs = algmAttrs;
	}
	public List<JoinPoint> getInputs() {
		return inputs;
	}
	public void setInputs(List<JoinPoint> inputs) {
		this.inputs = inputs;
	}
	public List<JoinPoint> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<JoinPoint> outputs) {
		this.outputs = outputs;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	public static ParamsVO transform(String params) throws Exception{
		JsonNode root = JSONUtils.getObjectMapper().readTree(params);
		ParamsVO vo=new ParamsVO();
		vo.setCode(root.path(AlgorithmConstants.ALGORITHM_CODE_KEY).asText());
		Map<String,Object> map=JSONUtils.getObjectMapper().readValue(root.path(AlgorithmConstants.ALGORITHM_ATTRS_KEY),Map.class);
		vo.setAlgmAttrs(map);
		vo.setInputs(transformFromJson((ArrayNode)root.path(AlgorithmConstants.DATA_INPUT_KEY)));
		vo.setOutputs(transformFromJson((ArrayNode)root.path(AlgorithmConstants.DATA_OUTPUT_KEY)));
		vo.setCallbackUrl(root.path(AlgorithmConstants.CALLBACK_URL_KEY).asText());
		return vo;
	}
	
	private static List<JoinPoint> transformFromJson(ArrayNode arr) throws Exception{
		List<JoinPoint> list=new ArrayList<>();
		for(JsonNode node:arr){
			JoinPoint jp=new JoinPoint();
			jp.setCode(node.path(DataMetaInfoConstants.JOIN_POINT_CODE_KEY).asText());
			jp.setDataKey(node.path(DataMetaInfoConstants.DATA_KEY).asText());
			String[] attrNames=new String[0];
			if(node.has(DataMetaInfoConstants.ATTR_NAMES_KEY)){
				ArrayNode an=(ArrayNode)node.path(DataMetaInfoConstants.ATTR_NAMES_KEY);
				attrNames=new String[an.size()];
				for(int i=0;i<attrNames.length;i++){
					attrNames[i]=an.get(i).asText();
				}
			}
			jp.setAttrNames(attrNames);
			list.add(jp);
		}
		return list;
	}

}
