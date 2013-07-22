package it.osg.utils;

import it.osg.service.model.Edge;
import it.osg.service.model.GraphElement;
import it.osg.service.model.Node;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
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
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			String nodeID = (String) ent.getProperty("nodeID");
			String nodeLabel = (String) ent.getProperty("nodeLabel");
			double nodeSize = (Double) ent.getProperty("nodeSize");
			String tipo = (String) ent.getProperty("nodeType");
			Node currNode = new Node(nodeID, nodeLabel, nodeSize, GraphElement.getType(tipo));
			result.add(currNode);
		}
		return result;

	}

	public static ArrayList<Edge> getEdges (String table, String idTransaction) {
		ArrayList<Edge> result = new ArrayList<Edge>();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			String edgeSource = (String) ent.getProperty("edgeSource");
			String edgeTarget = (String) ent.getProperty("edgeTarget");
			double edgeWeight = (Double) ent.getProperty("edgeWeight");
			String edgeType = (String) ent.getProperty("edgeType");
			Edge currEdge = new Edge(edgeSource, edgeTarget, edgeWeight, GraphElement.getType(edgeType));
			result.add(currEdge);
		}
		return result;

	}

	public static void incrementTask(String table, String idTransaction) {
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			Long exexutedTask = (Long) ent.getProperty("executedtask");
			ent.setProperty("executedtask", exexutedTask + 1L);
			DS.put(ent);
			return;
		}
		Entity newEnt = new Entity("task", idTransaction);
		newEnt.setProperty("executedtask", 1L);
		DS.put(newEnt);
	}

	public static Object getValue(String table, String filterPropertyName, String filterPropertyValue, String returnProperty) {
		Query q;
		PreparedQuery pq;
		Filter idFilter = null;
		if (filterPropertyName.equalsIgnoreCase("key")) {
			idFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, KeyFactory.createKey(table, filterPropertyValue));
		} else {
			idFilter = new FilterPredicate(filterPropertyName, FilterOperator.EQUAL, filterPropertyValue);
		} 
		q = new Query(table).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			Object returnValue = ent.getProperty(returnProperty);
			return returnValue;
		}
		return null;
	}
	
	public static void addRow(String table, String propertyName, Object propertyValue) {
		Entity newEnt = new Entity(table);
		newEnt.setProperty(propertyName, propertyValue);
		DS.put(newEnt);
	}

}
