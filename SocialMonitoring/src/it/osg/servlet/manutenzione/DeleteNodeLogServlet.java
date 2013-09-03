package it.osg.servlet.manutenzione;

import it.osg.servlet.DeleteTableServlet;

public class DeleteNodeLogServlet extends DeleteTableServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String getTabella() {
		return "node";
	}


	
	
}
