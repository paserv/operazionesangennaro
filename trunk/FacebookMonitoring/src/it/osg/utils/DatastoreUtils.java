package it.osg.utils;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class DatastoreUtils {

	private static DatastoreService DS = DatastoreServiceFactory.getDatastoreService();

	public static ArrayList<String> getKeyNamesFromTable(String tableName) {
		ArrayList<String> result = new ArrayList<String>();

		Query q;
		PreparedQuery pq;
		q = new Query(tableName).setKeysOnly();
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			String currKey = String.valueOf(ent.getKey().getName());
			result.add(currKey);
		}

		return result;

	}

	public static void saveNode (String table, String idTransaction, Node node) {
		Entity currEntity = new Entity(table);
		currEntity.setProperty("nodeID", node.id);
		currEntity.setProperty("nodeLabel", node.label);
		currEntity.setProperty("nodeSize", node.size);
		currEntity.setProperty("nodeType", node.type.toString());
		currEntity.setProperty("idTransaction", idTransaction);
		//currEntity.setProperty("pageId", pageId);
		DS.put(currEntity);
	}

	public static void saveEdge (String table, String idTransaction, Edge edge) {
		Entity currEntity = new Entity(table);
		currEntity.setProperty("edgeSource", edge.source);
		currEntity.setProperty("edgeTarget", edge.target);
		currEntity.setProperty("edgeWeight", edge.weight);
		currEntity.setProperty("edgeType", edge.type.toString());
		currEntity.setProperty("idTransaction", idTransaction);
		//currEntity.setProperty("pageId", pageId);
		DS.put(currEntity);
	}


	public static void saveNodes (String table, String idTransaction, ArrayList<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();
		while (iter.hasNext()) {
			Node currNode = iter.next();
			saveNode(table, idTransaction, currNode);
		}
	}

	public static void saveNodes (String table, String idTransaction, Hashtable<String, Node> nodes) {
		Enumeration<String> keys = nodes.keys();
		while(keys.hasMoreElements()) {
			Node currNode = nodes.get(keys.nextElement());
			saveNode(table, idTransaction, currNode);
		}
	}

	public static void saveEdges (String table, String idTransaction, ArrayList<Edge> edges) {
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge currEdge = iter.next();
			saveEdge(table, idTransaction, currEdge);
		}
	}

	public static void saveEdges (String table, String idTransaction, Hashtable<String, Edge> edges) {
		Enumeration<String> keys = edges.keys();
		while(keys.hasMoreElements()) {
			Edge currEdge = edges.get(keys.nextElement());
			saveEdge(table, idTransaction, currEdge);
		}
	}

	public static ArrayList<Node> getNodes (String table, String idTransaction) {
		ArrayList<Node> result = new ArrayList<Node>();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate(idTransaction, FilterOperator.EQUAL, idTransaction);
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {

		}
		
		return result;
		
	}

	public static void incrementTask(String table, String idTransaction) {
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate(idTransaction, FilterOperator.EQUAL, idTransaction);
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			ent.getProperty("executedtask");
		}

	}

}
