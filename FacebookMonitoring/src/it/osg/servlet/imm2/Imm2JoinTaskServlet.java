package it.osg.servlet.imm2;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.GephiUtils;
import it.osg.utils.MailUtils;
import it.osg.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

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

			Hashtable<String, Node> joinedNodes = aggregateNodes(nodes);
			Hashtable<String, Edge> joinedEdges = aggregateEdges(edges);			

			//INVIA MAIL
			String bodyMail = "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
			String attachFile = GephiUtils.createGraph(joinedNodes, joinedEdges);
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

	private Hashtable<String, Edge> aggregateEdges(ArrayList<Edge> edges) {
		Hashtable<String, Edge>  result = new Hashtable<String, Edge>();
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge currEdge = iter.next();
			if (!result.containsKey(currEdge.source + "_" + currEdge.target)) {
				result.put(currEdge.source + "_" + currEdge.target, currEdge);
			} else {
				Edge presentEdge = result.get(currEdge.source + "_" + currEdge.target);
				presentEdge.weight = Utils.trunkateToMax(presentEdge.weight + currEdge.weight, 50);
			}
		}
		return result;
	}

	private Hashtable<String, Node> aggregateNodes(ArrayList<Node> nodes) {
		Hashtable<String, Node>  result = new Hashtable<String, Node>();
		Iterator<Node> iter = nodes.iterator();
		while (iter.hasNext()) {
			Node currNode = iter.next();
			if (!result.containsKey(currNode.id)) {
				result.put(currNode.id, currNode);
			} else {
				Node presentNode = result.get(currNode.id);
				presentNode.size = Utils.trunkateToMax(presentNode.size + currNode.size, 100);
			}
		}
		return result;
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
