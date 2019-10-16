package com.datasphere.engine.manager.resource.provider.dictionary.model.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datasphere.engine.core.common.BaseController;
import com.datasphere.engine.manager.resource.provider.dictionary.model.DSSWord;

@Controller("/dict")
public class DictionaryController extends BaseController {

	@Resource(name="dictionaryService")
//	DictionaryService dictionaryService;
	
	@RequestMapping(value = "/listBy", method = RequestMethod.GET) 
	public Object listBy(DSSWord word) {
//		return JsonWrapper.successWrapper(dictionaryService.listBy(word));
		return null;
	}
	
	@RequestMapping(value = "/listForTree", method = RequestMethod.GET) 
	public Object listForTree(DSSWord word) {
//		return JsonWrapper.successWrapper(dictionaryService.listForTree(word));
		return null;
	}
}
