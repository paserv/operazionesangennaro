package it.osg.servlet.manutenzione;

import it.osg.servlet.DeleteTableServlet;

public class DeleteBaseInfoServlet extends DeleteTableServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String getTabella() {
		return "baseinfo";
	}

}
