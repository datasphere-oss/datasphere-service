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

package com.datasphere.engine.shaker.processor.instance.model;

import java.util.LinkedList;
import java.util.List;

public class GraphNode {
	List<NodePoint> prevs = new LinkedList<NodePoint>();
	List<NodePoint> nexts = new LinkedList<NodePoint>();
	ComponentInstance payload;
	
	public GraphNode(ComponentInstance c) {
		this.payload = c;
	}
	
	public class NodePoint {
		String point;
		GraphNode node;
		public NodePoint() {}
		public NodePoint(String point, GraphNode node) {
			this.point = point;
			this.node = node;
		}
		public String getPoint() {
			return point;
		}
		public GraphNode getNode() {
			return node;
		}
	}
	
	public void setPrev(String in, GraphNode node) {
		prevs.add(new NodePoint(in, node));
	}

	public void setNext(String out, GraphNode node) {
		nexts.add(new NodePoint(out, node));
	}
	
	public ComponentInstance getPayload() {
		return payload;
	}
	
	public List<NodePoint> nexts() {
		return nexts;
	}
	
	public List<NodePoint> prevs() {
		return prevs;
	}
}
