package it.osg.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class TestCreateTask extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		String strCallResult = "";
		resp.setContentType("text/plain");
		try {
			//Extract out the To, Subject and Body of the Email to be sent
			String strEmailId = req.getParameter("emailid");

			//Do validations here. Only basic ones i.e. cannot be null/empty

			if (strEmailId == null) throw new Exception("Email Id field cannot be empty.");

			//Trim the stuff
			strEmailId = strEmailId.trim();
			if (strEmailId.length() == 0) throw new Exception("Email Id field cannot be empty.");
			Queue queue = QueueFactory.getDefaultQueue();
			//Queue queue = QueueFactory.getQueue("subscription-queue");

			queue.add(TaskOptions.Builder.withUrl("/test").param("emailid",strEmailId));
			strCallResult = "Successfully created a Task in the Queue";
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
