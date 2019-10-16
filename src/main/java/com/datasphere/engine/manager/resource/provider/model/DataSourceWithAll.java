package com.datasphere.engine.manager.resource.provider.model;

import java.util.LinkedList;
import java.util.List;

public class DataSourceWithAll extends DataSource {
	// 面板数量
	Long panelCount;
	
	List<Object> panels = new LinkedList();
	
	public Long getPanelCount() {
		return panelCount;
	}

	public void setPanelCount(Long panelCount) {
		this.panelCount = panelCount;
	}

	public List getPanels() {
		return panels;
	}

	public void setPanels(List panels) {
		this.panels = panels;
	}
	
	public void addPanels(Object object) {
		this.panels.add(object);
	}
	
}
