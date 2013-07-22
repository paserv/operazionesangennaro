package it.osg.servlet.imm2;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;
import it.osg.utils.ArrayUtils;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.GephiUtils;
import it.osg.utils.MailUtils;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class Imm2JoinTaskServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final long timeout = 1000000L;


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
			ArrayList<Node> nodes = DatastoreUtils.getNodes("node", idTransaction);
			ArrayList<Edge> edges = DatastoreUtils.getEdges("edge", idTransaction);

			//			ArrayList<Node> joinNodes = ArrayUtils.removeDuplicate(list)

			//INVIA MAIL
			String bodyMail = "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
			String attachFile = GephiUtils.createGraph(nodes, edges);
			String subject = "Dati relativi alla pagina con ID " + pageId;
			MailUtils.sendMail(mail, subject, bodyMail, pageId + ".gexf", attachFile);

		} else {
			if (timeout - elapsedTime > 0) {
				//RIMETTE IN CODA SE STESSO DOPO UN CERTO DELAY
				try {
					Thread.sleep(100000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//TASK CHE MONITORA GLI ALTRI TASK (JOINTASKSERVLET)
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/imm2jointask").param("numTask", numTask).param("idTransaction", idTransaction).param("from", from).param("to", to).param("mail", mail).param("timestamp", timestamp).param("pageId", pageId));	
			} else {
				//TODO SALVA IL SALVABILE ED INVIA LA MAIL
			}
		}




	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	private boolean isTransactionEnded (String idTransaction, String numTask) {
		Long executedTask = (Long) DatastoreUtils.getValue("task", "key", idTransaction, "executedtask");
		if (executedTask != null && executedTask.compareTo(Long.valueOf(numTask)) == 0) {
			return true;
		} else {
			return false;
		}
	}


}
