package it.osg.servlet;

import it.osg.utils.DatastoreUtils;
import it.osg.utils.MailUtils;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public abstract class JoinTaskServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String pageId = "";
	protected String idTransaction = "";
	protected String from = "";
	protected String to = "";
	protected String timestamp = "";
	protected long elapsedTime = 0L;
	
	protected abstract long getTimeout();
	protected abstract long getDelay();
	
	protected abstract String getTaskTable();
	protected abstract String getExecutedTaskField();
	
	protected abstract String getSubjectMail();
	protected abstract String getBodyMail();
	protected abstract String getAttachFileName();
	protected abstract String getAttachFile();
	protected abstract String getJoinTaskName();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String numTask = req.getParameter("numTask");
		idTransaction = req.getParameter("idTransaction");
		from = req.getParameter("from");
		to = req.getParameter("to");
		String mail = req.getParameter("mail");
		timestamp = req.getParameter("timestamp");
		pageId = req.getParameter("pageId");

		elapsedTime = (System.currentTimeMillis() - Long.valueOf(timestamp))/1000;

		if (isTransactionEnded(idTransaction, numTask)) {

			MailUtils.sendMail(mail, getSubjectMail(), getBodyMail(), getAttachFileName(), getAttachFile());

		} else {
			if (getTimeout() - elapsedTime > 0) {
				//RIMETTE IN CODA SE STESSO DOPO UN CERTO DELAY
				try {
					Thread.sleep(getDelay());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//TASK CHE MONITORA GLI ALTRI TASK (JOINTASKSERVLET)
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/" + getJoinTaskName()).param("numTask", numTask).param("idTransaction", idTransaction).param("from", from).param("to", to).param("mail", mail).param("timestamp", timestamp).param("pageId", pageId));	
			} else {
				//TODO SALVA IL SALVABILE ED INVIA LA MAIL
			}
		}

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	private boolean isTransactionEnded (String idTransaction, String numTask) {
		Long executedTask = (Long) DatastoreUtils.getValue(getTaskTable(), "key", idTransaction, getExecutedTaskField());
		if (executedTask != null && executedTask.compareTo(Long.valueOf(numTask)) == 0) {
			return true;
		} else {
			return false;
		}
	}


}
