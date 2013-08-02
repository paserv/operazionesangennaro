package it.osg.servlet.baseinfo;

import it.osg.servlet.SplitTaskServlet;

public class BaseInfoSplitTaskServlet extends SplitTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getStep() {
		return Integer.parseInt((String.valueOf("1000000000")));
	}

	@Override
	public String getSubtask() {
		return "baseinfosubtask";
	}

	@Override
	public String getJointask() {
		return "baseinfojointask";
	}

	@Override
	public String getQueueName() {
		return "baseinfo";
	}



}
