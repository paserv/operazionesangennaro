package it.osg.servlet;

import it.osg.utils.MailUtils;
import it.osg.utils.ShardedCounter;

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
	
	protected abstract long getTimeout();
	protected abstract long getDelay();
	
	protected abstract String getSubjectMail(String pageId);
	protected abstract String getBodyMail(String from, String to, String timestamp, String pageId, long elapsedTime);
	protected abstract String getAttachFileName(String pageId);
	protected abstract String getAttachFile(String idTransaction);
	protected abstract String getJoinTaskName();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String numTask = req.getParameter("numTask");
		String idTransaction = req.getParameter("idTransaction");
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String mail = req.getParameter("mail");
		String timestamp = req.getParameter("timestamp");
		String pageId = req.getParameter("pageId");

		long elapsedTime = (System.currentTimeMillis() - Long.valueOf(timestamp))/1000;

		if (isTransactionEnded(idTransaction, numTask)) {

			MailUtils.sendMail(mail, getSubjectMail(pageId), getBodyMail(from, to, timestamp, pageId, elapsedTime), getAttachFileName(pageId), getAttachFile(idTransaction));
			ShardedCounter counter = new ShardedCounter();
			counter.delete(idTransaction);

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
		ShardedCounter counter = new ShardedCounter();
		long totalTask = Long.valueOf(numTask);
		long executedTask = counter.getCount(idTransaction);
		return totalTask == executedTask;
	}


}
