package it.osg.servlet.imm2;

import it.osg.servlet.SplitTaskServlet;
import it.osg.utils.DatastoreUtils;

public class Imm2SplitTaskServlet extends SplitTaskServlet {

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
		return "imm2subtask";
	}

	@Override
	public String getJointask() {
		return "imm2jointask";
	}

	@Override
	public String getQueueName() {
		return "imm2";
	}



}
