package it.osg.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;

public class GephiUtils {

	public static String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\" xmlns:viz=\"http://www.gexf.net/1.2draft/viz\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\">\n" +
			"\t<meta lastmodifieddate=\"2013-07-15\">\n" +
			"\t\t<creator>Don Pablo</creator>\n" +
			"\t\t<description></description>\n" +
			"\t</meta>\n" +
			"\t<graph defaultedgetype=\"directed\" mode=\"static\">\n";
	public static String FOOTER = "\t</graph>\n" +
			"</gexf>";
	
	public static String insertNode(Node nod) {
		return "<node id=\"" + nod.id + "\" label=\"" + nod.label + "\">\n" +
				"<attvalues></attvalues>\n" +
				"<viz:size value=\"" + nod.size + "\"></viz:size>\n" +
				"<viz:position x=\"0.0\" y=\"0.0\" z=\"0.0\"></viz:position>\n" +
				"<viz:color r=\"25\" g=\"139\" b=\"73\"></viz:color>\n" +
				"</node>\n";
	}

	public static String insertEdge(Edge currEdge) {
		return "<edge source=\"" + currEdge.source + "\" target=\"" + currEdge.target + "\" weight=\"" + currEdge.weight + "\">\n" +
				"<viz:color r=\"255\" g=\"0\" b=\"0\"></viz:color>\n" +
				"<attvalues></attvalues>\n" +
				"</edge>\n";
	}

	public static String createGraph(Hashtable<String,Node> nodes, Hashtable<String,Edge> edges) {
		String result = GephiUtils.HEADER;
		Enumeration<Node> iterNodes = nodes.elements();
		
		result = result + "<nodes>\n";
		while (iterNodes.hasMoreElements()) {
			Node currNode = iterNodes.nextElement();
			result = result + GephiUtils.insertNode(currNode);
		}
		result = result + "</nodes>\n";
		result = result + "<edges>\n";
		Enumeration<Edge> iterEdge = edges.elements();
		while (iterEdge.hasMoreElements()) {
			Edge currEdge = iterEdge.nextElement();
			result = result + GephiUtils.insertEdge(currEdge);
		}
		result = result + "</edges>\n";
		result = result + GephiUtils.FOOTER;
		return result;
	}
	
}
