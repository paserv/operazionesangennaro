package it.osg.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "graph")
public class Graph {

	String axis;
	Long ordinate;

	public Graph () {

	}

	public Graph (String axisIn, Long ordIn) {
		setAxis(axisIn);
		setOrdinate(ordIn);
	}

	public String getAxis() {
		return axis;
	}


	public void setAxis(String axis) {
		this.axis = axis;
	}


	public Long getOrdinate() {
		return ordinate;
	}
	public void setOrdinate(Long ordinate) {
		this.ordinate = ordinate;
	}

	@Override
	public String toString() {
		return axis.toString() + " " + ordinate.toString();
	}



}
