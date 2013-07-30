package it.osg.servlet.imm2;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;
import it.osg.servlet.JoinTaskServlet;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.GephiUtils;
import it.osg.utils.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class Imm2JoinTaskServlet extends JoinTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected long getTimeout() {
		return (Long) DatastoreUtils.getValue("conf", "property", "jointimeout", "value");
	}


	@Override
	protected long getDelay() {
		return (Long) DatastoreUtils.getValue("conf", "property", "joindelay", "value");
	}


	@Override
	protected String getSubjectMail() {
		return "Dati relativi alla pagina con ID " + pageId;
	}


	@Override
	protected String getBodyMail() {
		return "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
	}


	@Override
	protected String getAttachFileName() {
		return pageId + ".gexf";
	}


	@Override
	protected String getAttachFile() {
		ArrayList<Node> nodes = DatastoreUtils.getNodes("node", idTransaction);
		ArrayList<Edge> edges = DatastoreUtils.getEdges("edge", idTransaction);

		Hashtable<String, Node> joinedNodes = aggregateNodes(nodes);
		Hashtable<String, Edge> joinedEdges = aggregateEdges(edges);
		
		String attachFile = GephiUtils.createGraph(joinedNodes, joinedEdges, getBodyMail());
		
		return attachFile;
	}


	@Override
	protected String getJoinTaskName() {
		return "imm2jointask";
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


}
