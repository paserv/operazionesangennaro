package it.osg.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "graph")
public class Graph {

	String axis;
	Long ordinate;
	double ordinateD;

	public Graph () {

	}

	public Graph (String axisIn, Long ordIn) {
		setAxis(axisIn);
		setOrdinate(ordIn);
	}
	
	public Graph (String axisIn, double ordIn) {
		setAxis(axisIn);
		setOrdinateD(ordIn);
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

	
	
	public double getOrdinateD() {
		return ordinateD;
	}

	public void setOrdinateD(double ordinateD) {
		this.ordinateD = ordinateD;
	}

	@Override
	public String toString() {
		return axis.toString() + " " + ordinate.toString();
	}



}
