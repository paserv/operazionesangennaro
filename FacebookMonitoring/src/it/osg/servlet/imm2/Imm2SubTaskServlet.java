package it.osg.servlet.imm2;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.service.model.Edge;
import it.osg.service.model.GraphElement.ElementType;
import it.osg.service.model.Node;
import it.osg.servlet.SubTaskServlet;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.ShardedCounter;
import it.osg.utils.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class Imm2SubTaskServlet extends SubTaskServlet  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void runSubTask() {

		//OUTPUT DATA
		Hashtable<String, Node> nodes = new Hashtable<String, Node>();
		Hashtable<String, Edge> edges = new Hashtable<String, Edge>();


		//Get all Post
		ArrayList<Post> posts = FacebookUtils.getAllPosts(pageId, from, to, null);
		ArrayList<Post> postFromPage = new ArrayList<Post>();
		ArrayList<Post> postFromFan = new ArrayList<Post>();

		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post currPost = iterPost.next();
			if (currPost.getFrom().getId().equals(pageId)) {
				postFromPage.add(currPost);
			} else {
				postFromFan.add(currPost);
			}
		}

		//Get all Likes and Shares to Posts
		double sizeLikes = 1;
		double parzShares = 1;
		double parzNumPost = 1;

		ArrayList<Like> parzLikes = FacebookUtils.getLikes(postFromPage);
		sizeLikes = sizeLikes + parzLikes.size();
		parzShares = parzShares + FacebookUtils.getShares(postFromPage);
		parzNumPost = parzNumPost + postFromFan.size();

		Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(pageId);

		//AGGIUNGO NODO FACEBOOK ID
		Node pageIdNode = new Node(pageId, (String) baseInfo.get("name"), Utils.trunkateToMax((parzShares+parzLikes.size())/parzNumPost, 100), ElementType.PAGEID);
		nodes.put(pageId, pageIdNode);

		//Prendo i Commenti ai Post e li raggruppo per ID_AUTORE_COMMENTO
		ArrayList<Comment> commentsPostFromPage = FacebookUtils.getComments(postFromPage);
		Iterator<Comment> iterComm = commentsPostFromPage.iterator();
		Hashtable<String, ArrayList<Comment>> commHash = new Hashtable<String, ArrayList<Comment>>();
		while (iterComm.hasNext()) {
			Comment currComm = iterComm.next();
			//TODO trovare anche le risposte ai commenti
			String nameFrom = currComm.getFrom().getId();
			if (!commHash.containsKey(nameFrom)) {
				ArrayList<Comment> currComms = new ArrayList<Comment>();
				currComms.add(currComm);
				commHash.put(nameFrom, currComms);
			} else  {
				ArrayList<Comment> addComm = commHash.get(nameFrom);
				addComm.add(currComm);
			}
		}

		//Ora che sono raggruppati per ID_AUTORE_COMMENTO creo i NODE (con raggio pari ai likes ricevuti dai commenti)
		//e creo le EDGE (con weight pari al numero di Commenti che l'autore ha fatto ai Post della Fan Page Facebook)
		Enumeration<String> keys = commHash.keys();
		while (keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			ArrayList<Comment> commentsForAuthor = commHash.get(currKey);
			//AGGIUNGO NODO AUTHOR DI COMMENTI
			double sizeAuthorNode = 1;
			sizeAuthorNode = sizeAuthorNode + FacebookUtils.getCommentLike(commentsForAuthor);
			Node authorNode = new Node(currKey, Utils.cleanString(commentsForAuthor.get(0).getFrom().getName()), sizeAuthorNode, ElementType.AUTHOR);
			nodes.put(currKey, authorNode);
			//AGGIUNGO LA EDGE
			double weightEdge = 1;
			weightEdge = weightEdge + commentsForAuthor.size();
			Edge currEdge = new Edge(currKey, pageId, weightEdge, ElementType.AUTHOR);
			edges.put(currKey + "_" + pageId, currEdge);
		}

		//TODO aggiungere NODI ed EDGEs per quello che riguarda i post dei fan sulla bacheca
		//				ArrayList<Comment> commentsPostFromFan = FacebookUtils.getComments(postFromFan);
		//				Iterator<Comment> iterFanPost = commentsPostFromFan.iterator();

		//SAVE NODES AND EDGES TO DATASTORE AND INCREMENT 1 TASK
		Hashtable<String, Node> myNodes = DatastoreUtils.saveNodes("node", idTransaction, nodes, (Long) DatastoreUtils.getValue("conf", "property", "minnodesize", "value"));
		DatastoreUtils.saveEdges("edge", idTransaction, edges, myNodes);
		ShardedCounter sc = new ShardedCounter();
		sc.increment(idTransaction);
	}



}
