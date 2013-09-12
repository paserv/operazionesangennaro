package it.osg.servlet.monitoring.facebook.sindaci;

import it.osg.servlet.monitoring.MonitoringToQueueServlet;

public class MonitoringFacebookServletQueue extends MonitoringToQueueServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMonitorEntityId() {
		return "facebookmonitorsindaci";
	}

	
}
