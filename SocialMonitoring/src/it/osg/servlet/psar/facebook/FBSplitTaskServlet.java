package it.osg.servlet.psar.facebook;

import it.osg.servlet.SplitTaskServlet;
import it.osg.utils.DatastoreUtils;

public class FBSplitTaskServlet extends SplitTaskServlet {

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
		return "FBpsarsubtask";
	}


	@Override
	public String getJointask() {
		return "FBpsarjointask";
	}


	@Override
	public String getQueueName() {
		return "default";
	}


}
