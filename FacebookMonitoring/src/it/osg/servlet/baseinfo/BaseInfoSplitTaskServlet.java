package it.osg.servlet.baseinfo;

import it.osg.servlet.SplitTaskServlet;
import it.osg.utils.DatastoreUtils;

public class BaseInfoSplitTaskServlet extends SplitTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getStep() {
		long res = (Long) DatastoreUtils.getValue("conf", "property", "splitstep", "value");
		return Integer.parseInt((String.valueOf(res)));
	}

	@Override
	public String getSubtask() {
		return "baseinfosubtask";
	}

	@Override
	public String getJointask() {
		return "baseinfojointask";
	}



}
