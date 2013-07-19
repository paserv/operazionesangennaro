package it.osg.servlet.imm2;

import it.osg.servlet.SplitTaskServlet;

public class Imm2SplitTaskServlet extends SplitTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getStep() {
		return 4;
	}

	@Override
	public String getSubtask() {
		return "imm2subtask";
	}

	@Override
	public String getJointask() {
		return "imm2jointask";
	}



}
