package it.osg.service.model;

public class Node extends GraphElement {

	public String id;
	public String label;
	public double size;

	
	public Node(String ident, String lab, double siz, ElementType tp) {
		this.id = ident;
		this.label = lab;
		this.size = siz;
		this.type = tp;
	}
	
	
}
