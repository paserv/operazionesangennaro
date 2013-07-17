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

public class DatastoreUtils {

	private static DatastoreService getDS() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	
	public static ArrayList<String> getKeyNamesFromTable(String tableName) {
		ArrayList<String> result = new ArrayList<String>();
		
		DatastoreService datastore = getDS();
		Query q;
		PreparedQuery pq;
		q = new Query(tableName).setKeysOnly();
		pq = datastore.prepare(q);
		for (Entity ent : pq.asIterable()) {
			String currKey = String.valueOf(ent.getKey().getName());
			result.add(currKey);
		}
		
		return result;
		
	}
	
	public static void saveNode (String table, Node node) {
		DatastoreService datastore = getDS();
		Entity currEntity = new Entity(table);
		currEntity.setProperty("nodeID", node.id);
		currEntity.setProperty("nodeLabel", node.label);
		currEntity.setProperty("nodeSize", node.size);
		currEntity.setProperty("nodeType", node.type.toString());
		datastore.put(currEntity);
	}
	
	public static void saveEdge (String table, Edge edge) {
		DatastoreService datastore = getDS();
		Entity currEntity = new Entity(table);
		currEntity.setProperty("edgeSource", edge.source);
		currEntity.setProperty("edgeTarget", edge.target);
		currEntity.setProperty("edgeWeight", edge.weight);
		currEntity.setProperty("edgeType", edge.type.toString());
		datastore.put(currEntity);
	}
	
	
	public static void saveNodes (String table, ArrayList<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();
		while (iter.hasNext()) {
			Node currNode = iter.next();
			saveNode(table, currNode);
		}
	}
	
	public static void saveNodes (String table, Hashtable<String, Node> nodes) {
		Enumeration<String> keys = nodes.keys();
		while(keys.hasMoreElements()) {
			Node currNode = nodes.get(keys.nextElement());
			saveNode(table, currNode);
		}
	}
	
	public static void saveEdges (String table, ArrayList<Edge> edges) {
		Iterator<Edge> iter = edges.iterator();
		while (iter.hasNext()) {
			Edge currEdge = iter.next();
			saveEdge(table, currEdge);
		}
	}
	
	public static void saveEdges (String table, Hashtable<String, Edge> edges) {
		Enumeration<String> keys = edges.keys();
		while(keys.hasMoreElements()) {
			Edge currEdge = edges.get(keys.nextElement());
			saveEdge(table, currEdge);
		}
	}
	
}
