package com.datasphere.engine.manager.resource.provider.dictionary.model.service;

import com.datasphere.engine.core.common.BaseService;
import com.datasphere.engine.manager.resource.provider.dictionary.model.DSSWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DictionaryService extends BaseService {


	public List<DSSWord> listForTree(DSSWord word) {
//		List<JusfounWord> list = dao.listBy(word);
		return toTreeList(null);
	}
	
	public static List<DSSWord> toTreeList(List<DSSWord> list) {
		List<DSSWord> treeList = new ArrayList<DSSWord>();
		Map<String, DSSWord> map = new HashMap<String, DSSWord>();
		for(DSSWord word: list) {
			if(word.getParent() == null) {
				map.put(word.getCode(), word);
				treeList.add(word);
			} else {
				for(DSSWord item: list) {
					if(word.getParent().equals(item.getCode())) {
						item.addChild(word);
						break;
					}
				}
			}
		}
		return treeList;
	}
}
