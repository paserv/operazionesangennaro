package it.osg.servlet;

import it.osg.utils.DateUtils;
import it.osg.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class SplitTaskServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String pageId = req.getParameter("pageId");
		String from = req.getParameter("from");
		String to = req.getParameter("to");	
		String mail = req.getParameter("mail");

		String strCallResult = "";
		resp.setContentType("text/plain");
		try  {
			if (pageId == null || from == null || to == null || mail == null) throw new Exception("Fields cannot be empty.");
			if (mail.length() == 0) throw new Exception("To field cannot be empty.");
			strCallResult = "Successfully created a Task in the Queue.\nYou will receive an email as data are available";
			resp.getWriter().println(strCallResult);

			String timestamp = String.valueOf(System.currentTimeMillis());

			String idTransaction = Utils.MD5(pageId + mail + timestamp);

			//GET TO DATE	
			Date t = null;
			try {
				t = DateUtils.parseDateAndTime(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			//SPLITTO UN GIORNO ALLA VOLTA ED INVIO IN CODA
			Queue queue = QueueFactory.getDefaultQueue();
			String from1 = from;
			int numTask = 0;
			while (true) {
				Date f1 = null;
				try {
					f1 = DateUtils.parseDateAndTime(from1);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				from1 = DateUtils.formatDateAndTime(f1);
				Date t1 = DateUtils.addOneDay(f1);
				String to1 = DateUtils.formatDateAndTime(t1);

				if (DateUtils.compareDate(t, t1) >= 0) {
					String paramFrom = from1.substring(0, 10) + " 00:00:00";
					String paramTo = to1.substring(0, 10) + " 23:59:59";

					queue.add(TaskOptions.Builder.withUrl("/subtask").param("idTransaction", idTransaction).param("from", paramFrom).param("to", paramTo).param("pageId", pageId));
					numTask = numTask + 1;
					from1 = DateUtils.formatDateAndTime(t1);

				} else {
					break;
				}
			}

			
			//TASK CHE MONITORA GLI ALTRI TASK (JOINTASKSERVLET)
			queue.add(TaskOptions.Builder.withUrl("/jointask").param("idTransaction", idTransaction).param("numTask", String.valueOf(numTask)).param("from", from).param("to", to).param("pageId", pageId).param("mail", mail).param("timestamp", timestamp));	


		} catch (Exception ex) {
			strCallResult = "Fail: " + ex.getMessage();
			resp.getWriter().println(strCallResult);
		}




	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}


}
