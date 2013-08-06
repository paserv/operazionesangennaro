package it.osg.servlet;

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
