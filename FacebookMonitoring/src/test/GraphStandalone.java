package test;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.service.model.Edge;
import it.osg.service.model.Node;
import it.osg.service.model.GraphElement.ElementType;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.GephiUtils;
import it.osg.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class GraphStandalone {

	public static void main(String[] args) {

		//INPUT DATA
		String from = "01-07-2013 00:00:00";
		String to = "23-07-2013 00:00:00";
		ArrayList<String> pageIds = new ArrayList<String>();
		pageIds.add("166115370094396");
		pageIds.add("266848563334709");
		pageIds.add("467480896629246");
		pageIds.add("161895440519247");
		pageIds.add("48166220529");
		pageIds.add("281125725235741");

		//GET DATE
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(from);
			t = DateUtils.parseDateAndTime(to);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//OUTPUT DATA
		Hashtable<String, Node> nodes = new Hashtable<String, Node>();
		Hashtable<String, Edge> edges = new Hashtable<String, Edge>();

		Iterator<String> iter = pageIds.iterator();
		while (iter.hasNext()) {
			String currPageid = iter.next();

			//Get all Post
			ArrayList<Post> posts = FacebookUtils.getAllPosts(currPageid, f, t, null);
			ArrayList<Post> postFromPage = new ArrayList<Post>();
			ArrayList<Post> postFromFan = new ArrayList<Post>();

			Iterator<Post> iterPost = posts.iterator();
			while (iterPost.hasNext()) {
				Post currPost = iterPost.next();
				if (currPost.getFrom().getId().equals(currPageid)) {
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

			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(currPageid);

			//AGGIUNGO NODO FACEBOOK ID
			Node pageIdNode = new Node(currPageid, (String) baseInfo.get("name"), Utils.trunkateToMax(parzShares*parzNumPost*parzLikes.size(), 50), ElementType.PAGEID);
			nodes.put(currPageid, pageIdNode);

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
				if (nodes.containsKey(currKey)) {
					Node extraxtNode = nodes.get(currKey);
					extraxtNode.size = extraxtNode.size + sizeAuthorNode;
				} else {
					Node authorNode = new Node(currKey, Utils.cleanString(commentsForAuthor.get(0).getFrom().getName()), sizeAuthorNode, ElementType.AUTHOR);
					nodes.put(currKey, authorNode);
				}

				//AGGIUNGO LA EDGE
				double weightEdge = 1;
				weightEdge = weightEdge + commentsForAuthor.size();
				if (edges.containsKey(currKey + "_" + currPageid)) {
					Edge extractEdge = edges.get(currKey + "_" + currPageid);
					extractEdge.weight = extractEdge.weight + weightEdge;
				} else {
					Edge currEdge = new Edge(currKey, currPageid, weightEdge, ElementType.AUTHOR);
					edges.put(currKey + "_" + currPageid, currEdge);
				}

			}

			//TODO aggiungere NODI ed EDGEs per quello che riguarda i post dei fan sulla bacheca
			//			ArrayList<Comment> commentsPostFromFan = FacebookUtils.getComments(postFromFan);
			//			Iterator<Comment> iterFanPost = commentsPostFromFan.iterator();



		}


		Hashtable<String, Node> filNodes = filterNodesBySize(nodes, 10);
		Hashtable<String, Edge> filEdges = filterEdgesByWeight(edges, 1);

		String attachFile = GephiUtils.createGraph(filNodes, filEdges);
		System.out.println(attachFile);
		


	}


	private static Hashtable<String, Edge> filterEdgesByWeight (Hashtable<String,Edge> edges, double minweight) {
		Hashtable<String, Edge>  result = new Hashtable<String, Edge>();
		Enumeration<String> en = edges.keys();
		while (en.hasMoreElements()) {
			String currKey = en.nextElement();
			Edge currEdge = edges.get(currKey);
			if (currEdge.weight >= minweight) {
				result.put(currKey, currEdge);
			}
		}
		return result;
	}

	private static Hashtable<String, Node> filterNodesBySize(Hashtable<String, Node> nodes, double minsize) {
		Hashtable<String, Node>  result = new Hashtable<String, Node>();
		Enumeration<String> en = nodes.keys();
		while (en.hasMoreElements()) {
			String currKey = en.nextElement();
			Node currNode = nodes.get(currKey);
			if (currNode.size >= minsize) {
				result.put(currKey, currNode);
			}
		}
		return result;
	}

}
