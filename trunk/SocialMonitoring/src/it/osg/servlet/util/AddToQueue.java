package it.osg.servlet.util;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class AddToQueue extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		
		String param1 = req.getParameter("param1");
		String param2 = req.getParameter("param2");
		String param3 = req.getParameter("param3");
		String param4 = req.getParameter("param4");
		
		
		resp.setContentType("text/plain");
		String servlet = req.getParameter("servlet");
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/" + servlet).param(param1.split(",")[0], param1.split(",")[1]).param(param2.split(",")[0], param2.split(",")[1]).param(param3.split(",")[0], param3.split(",")[1]).param(param4.split(",")[0], param4.split(",")[1]));
		resp.getWriter().println("STABBENE");
	}

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	
	
}
