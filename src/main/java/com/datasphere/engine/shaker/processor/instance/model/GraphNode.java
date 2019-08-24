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
