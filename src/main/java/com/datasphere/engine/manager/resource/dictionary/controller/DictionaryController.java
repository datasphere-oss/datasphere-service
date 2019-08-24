package com.datasphere.engine.manager.resource.dictionary.controller;

import javax.annotation.Resource;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.server.dictionary.DSSWord;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

//@Validated
@Controller("/dict")
public class DictionaryController extends BaseController {

	@Resource(name="dictionaryService")
//	DictionaryService dictionaryService;
	
	@Get("/listBy")
	public Object listBy(DSSWord word) {
//		return JsonWrapper.successWrapper(dictionaryService.listBy(word));
		return null;
	}
	
	@Get("/listForTree")
	public Object listForTree(DSSWord word) {
//		return JsonWrapper.successWrapper(dictionaryService.listForTree(word));
		return null;
	}
}
