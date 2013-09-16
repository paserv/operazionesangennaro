package it.osg.servlet.util;


import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.IdNameEntity;
import facebook4j.Post;
import facebook4j.Post.Property;
import facebook4j.Tag;
import it.osg.model.Graph;
import it.osg.utils.Constants;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.MailUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		ArrayList<Graph> result = new ArrayList<Graph>();

		Date f = null;
		Date t = null;
		try {

			f = DateUtils.parseDateAndTime((String) "01-09-2013 00:00:00");
			t = DateUtils.parseDateAndTime((String) "16-09-2013 00:00:00");


			// Get the Datastore Service
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q;
			if (f != null && t != null) {
				Filter fromFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, f);
				Filter toFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, t);
				Filter transmissionFilter = new FilterPredicate("idFacebook", FilterOperator.EQUAL, (String) "113335124914");
				Filter fromToTransmissionFilter = CompositeFilterOperator.and(transmissionFilter, fromFilter, toFilter);
				q = new Query(Constants.FACEBOOK_MONITOR_TABLE).setFilter(fromToTransmissionFilter).addSort("date", SortDirection.ASCENDING);
			} else {
				q = new Query(Constants.FACEBOOK_MONITOR_TABLE).addSort("date", SortDirection.ASCENDING);
			}


			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);
			Long previousOrdinate = 0L;
			for (Entity ent : pq.asIterable()) {
				String axisReturned = "";
				Long ordinateReturned = 0L;

				Object datastoreAxis = ent.getProperty((String) "date");
				if (datastoreAxis instanceof Date) {
					axisReturned = DateUtils.formatDateAndTime((Date) datastoreAxis);
				} else {
					axisReturned = datastoreAxis.toString();
				}

				Object datastoreOrdinate = ent.getProperty((String) "like_count");
				if (datastoreOrdinate instanceof Long) {
					ordinateReturned = (Long) datastoreOrdinate;
					if (ordinateReturned.compareTo(0L) == 0) {
						ordinateReturned = previousOrdinate;
						out.println("ordinateReturned is 0 previous is " + ordinateReturned);
					} else {
						previousOrdinate = ordinateReturned;
						out.println("ordinateReturned is " + ordinateReturned);
					}
				} else {
					out.println("ordinateReturned is " + ordinateReturned + " previous is " + previousOrdinate);
					ordinateReturned = previousOrdinate;
				}

				Graph gd = new Graph(axisReturned, ordinateReturned);
				result.add(gd);
				out.println("Coordinate: " + gd.toString() + "<br>");

			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}



		//		String dateFrom = req.getParameter("from");
		//		String dateTo = req.getParameter("to");
		//		Date f = null;
		//		Date t = null;
		//		try {
		////			f = DateUtils.parseDateAndTime("01-09-2013 00:00:00");
		////			t = DateUtils.addDayToDate(f, 1);
		//			f = DateUtils.parseDateAndTime(dateFrom);
		//			t = DateUtils.parseDateAndTime(dateTo);
		//		} catch (ParseException e) {
		//			e.printStackTrace();
		//		}
		//		ArrayList<Post> posts = FacebookUtils.getAllPosts("113335124914", f, t, null);
		//		Iterator<Post> iterPost = posts.iterator();
		//		while (iterPost.hasNext()) {
		//			Post currPost = iterPost.next();
		//			out.println("Post ID: " + currPost.getId() + "<br>");
		//			out.println("Post Type: " + currPost.getType() + "<br>");
		//			out.println("Post From: " + currPost.getFrom().getName() + "<br>");
		//			out.println("Message: " + currPost.getMessage() + "<br>");
		//			
		//			List<Tag> tags = currPost.getMessageTags();
		//			if (tags != null) {
		//				Iterator<Tag> iterTags = tags.iterator();
		//				while (iterTags.hasNext()) {
		//					Tag currTag = iterTags.next();
		//					out.println("&nbsp;Tag Name: " + currTag.getName() + "<br>");
		//					out.println("&nbsp;Tag Type: " + currTag.getType() + "<br>");
		//					out.println("&nbsp;Tag Metadata: " + currTag.getMetadata() + "<br>");
		//				}
		//			}
		//			
		//			List<IdNameEntity> tos = currPost.getTo();
		//			if (tos != null) {
		//				Iterator<IdNameEntity> iterTo = tos.iterator();
		//				while (iterTo.hasNext()) {
		//					IdNameEntity currTo = iterTo.next();
		//					out.println("&nbsp;To Id: " + currTo.getId() + "<br>");
		//					out.println("&nbsp;To Name: " + currTo.getName() + "<br>");
		//				}
		//			}
		//			
		//			List<Property> props = currPost.getProperties();
		//			if (props != null) {
		//				Iterator<Property> iterProps = props.iterator();
		//				while (iterProps.hasNext()) {
		//					Property currProp = iterProps.next();
		//					out.println("&nbsp;Prop name: " + currProp.getName() + "<br>");
		//					out.println("&nbsp;Prop Text: " + currProp.getText() + "<br>");
		//				}
		//			}
		//			
		//			
		//			ArrayList<Comment> comms = FacebookUtils.getAllComments(currPost);
		//			Iterator<Comment> iterComms = comms.iterator();
		//			while (iterComms.hasNext()) {
		//				Comment currComm = iterComms.next();
		//				out.println("&nbsp;&nbsp;Comment ID: " + currComm.getId() + "<br>");
		//				out.println("&nbsp;&nbsp;Comment From: " + currComm.getFrom().getName() + "<br>");
		//				out.println("&nbsp;&nbsp;Comment Message: " + currComm.getMessage() + "<br>");
		//				out.println("&nbsp;&nbsp;Comment Metadata: " + currComm.getMetadata() + "<br>");
		//			}
		//		}
		//		




		//		String mail = "";
		//		
		////		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		////		Query q;
		////		PreparedQuery pq;
		////		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, "7182af0116b073f5299372a6e5be3c59");
		////		q = new Query("task").setFilter(idFilter);
		////		pq = datastore.prepare(q);
		////		int executedTask = pq.countEntities();
		////		out.println("AAAAAAA: " + executedTask);
		////		mail = mail + "ExTask: " + executedTask + "\n";
		//		
		//		
		//		Date f = null;
		//		Date t = null;
		//		try {
		//			f = DateUtils.parseDateAndTime("01-08-2013 00:00:00");
		//			t = DateUtils.parseDateAndTime("03-08-2013 00:00:00");
		//		} catch (ParseException e) {
		//			e.printStackTrace();
		//		}
		//
		//
		//		//Get all Post
		//		ArrayList<Post> posts = FacebookUtils.getAllPosts("113335124914", f, t, null);
		//
		//		ArrayList<Post> postFromPage = new ArrayList<Post>();
		//		ArrayList<Post> postFromFan = new ArrayList<Post>();
		//
		//		Iterator<Post> iterPost = posts.iterator();
		//		while (iterPost.hasNext()) {
		//			Post currPost = iterPost.next();
		//			if (currPost.getFrom().getId().equals("113335124914")) {
		//				postFromPage.add(currPost);
		//			} else {
		//				postFromFan.add(currPost);
		//			}
		//		}
		//
		//		//Get all Comments
		//		ArrayList<Comment> comments = FacebookUtils.getComments(postFromPage);		
		//
		//		//Get all Unique Authors of Comments
		//		ArrayList<String> uniqueAuth = FacebookUtils.getUniqueAuthors(comments);
		//		out.println("SIZE: " + uniqueAuth.size());
		//		mail = mail + "SIZE1: " + uniqueAuth.size() + "\n";
		////		Iterator<String> iter = uniqueAuth.iterator();
		////		while (iter.hasNext()) {
		////			out.println(iter.next() + "<br>");
		////		}
		//		
		//		
		//		Date f1 = null;
		//		Date t1 = null;
		//		try {
		//			f1 = DateUtils.parseDateAndTime("04-08-2013 00:00:00");
		//			t1 = DateUtils.parseDateAndTime("07-08-2013 00:00:00");
		//		} catch (ParseException e) {
		//			e.printStackTrace();
		//		}
		//
		//
		//		//Get all Post
		//		ArrayList<Post> posts1 = FacebookUtils.getAllPosts("113335124914", f1, t1, null);
		//
		//		ArrayList<Post> postFromPage1 = new ArrayList<Post>();
		//		ArrayList<Post> postFromFan1 = new ArrayList<Post>();
		//
		//		Iterator<Post> iterPost1 = posts1.iterator();
		//		while (iterPost1.hasNext()) {
		//			Post currPost = iterPost1.next();
		//			if (currPost.getFrom().getId().equals("113335124914")) {
		//				postFromPage1.add(currPost);
		//			} else {
		//				postFromFan1.add(currPost);
		//			}
		//		}
		//
		//		//Get all Comments
		//		ArrayList<Comment> comments1 = FacebookUtils.getComments(postFromPage1);		
		//
		//		//Get all Unique Authors of Comments
		//		ArrayList<String> uniqueAuth1 = FacebookUtils.getUniqueAuthors(comments1);
		//		out.println("SIZE: " + uniqueAuth1.size());
		//		mail = mail + "SIZE2: " + uniqueAuth1.size() + "\n";
		//		
		//		
		//		uniqueAuth.addAll(uniqueAuth1);
		//		out.println("SIZE: " + uniqueAuth.size());
		//		mail = mail + "SIZE3: " + uniqueAuth.size() + "\n";
		//		MailUtils.sendMail("paserv%40gmail.com", "Ciao", mail, "al", "dd");

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
