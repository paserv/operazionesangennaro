package it.osg.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class AddPsarToQueue extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		String strCallResult = "";
		resp.setContentType("text/plain");
		try {
			
			String sindaco = req.getParameter("sindaco");
			String from = req.getParameter("from");
			String to = req.getParameter("to");
			
			String mailTo = req.getParameter("mailTo");
			
			if (sindaco == null || from == null || to == null || mailTo == null) throw new Exception("Fields cannot be empty.");

			mailTo = mailTo.trim();
			if (mailTo.length() == 0) throw new Exception("To field cannot be empty.");
			
			Queue queue = QueueFactory.getDefaultQueue();
			//Queue queue = QueueFactory.getQueue("subscription-queue");

			queue.add(TaskOptions.Builder.withUrl("/psar").param("sindaco", sindaco).param("from", from).param("to", to).param("mailTo", mailTo));
			strCallResult = "Successfully created a Task in the Queue.\nYou will receive an email as data are available";
			resp.getWriter().println(strCallResult);
		}
		catch (Exception ex) {
			strCallResult = "Fail: " + ex.getMessage();
			resp.getWriter().println(strCallResult);
		}

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
