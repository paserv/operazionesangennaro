package it.osg.servlet.psar.plus;

import it.osg.servlet.SplitTaskServlet;
import it.osg.utils.DatastoreUtils;

public class PLSplitTaskServlet extends SplitTaskServlet {

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
		return "PLpsarsubtask";
	}


	@Override
	public String getJointask() {
		return "PLpsarjointask";
	}


	@Override
	public String getQueueName() {
		return "default";
	}


}
