package it.osg.servlet.util;


import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import it.osg.utils.ArrayUtils;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.MailUtils;
import it.osg.utils.PlusUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Comments;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject.Plusoners;
import com.google.api.services.plus.model.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(TestServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		out.println("106590037983789925298<br>");
		
		String pageId = "106590037983789925298";
		
		ArrayList<String> ids = DatastoreUtils.getPropertyList("anagraficaSindaco", "IDPlus");
		Iterator<String> iterids = ids.iterator();
		while (iterids.hasNext()) {
			String curr = iterids.next();
			out.println(curr + "<br>");
			
		}
		
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime("01-06-2013 00:00:00");
			t = DateUtils.parseDateAndTime("15-07-2013 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ArrayList<Activity> activities = PlusUtils.getAllPosts(pageId, f, t, null);
		
		ArrayList<Comment> comments = PlusUtils.getComments(activities);
		
		ArrayList<String> authors = PlusUtils.getUniqueAuthors(comments);
		out.println("Size: " + authors.size() + "<br>");
		Iterator<String> iter = authors.iterator();
		while (iter.hasNext()) {
			String curr = iter.next();
			out.println(curr + "<br>");
		}
		
		try {
			f = DateUtils.parseDateAndTime("15-07-2013 00:00:01");
			t = DateUtils.parseDateAndTime("15-08-2013 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		activities = PlusUtils.getAllPosts(pageId, f, t, null);
		
		comments = PlusUtils.getComments(activities);
		
		authors.addAll(PlusUtils.getUniqueAuthors(comments));
		authors = ArrayUtils.removeDuplicate(authors);
		out.println("Size: " + authors.size() + "<br>");
		Iterator<String> iter2 = authors.iterator();
		while (iter2.hasNext()) {
			String curr = iter2.next();
			out.println(curr + "<br>");
		}
		
		
		
//		out.println("Sized: " + activities.size() + "<br>");
//		Iterator<Activity> iterActivity = activities.iterator();
//		while (iterActivity.hasNext()) {
//			Activity currActivity = iterActivity.next();
//			out.println("<br><br><br>Autore: " + currActivity.getActor().getDisplayName() + "<br>");
//			out.println("Data: " + currActivity.getPublished() + "<br>");
//			out.println("Plusoners: " + currActivity.getObject().getPlusoners().size() + "<br>Total: " + currActivity.getObject().getPlusoners().getTotalItems() + "<br>");
//			out.println("PLUS STRING: " + currActivity.getObject().getPlusoners().toPrettyString() + "<br>");
//			out.println("Replies: " + currActivity.getObject().getReplies().size() + "<br>");
//			out.println("Replies: " + currActivity.getObject().getReplies().toPrettyString() + "<br>");
//			out.println("Shares: " + currActivity.getObject().getResharers().size() + "<br>");
//			out.println("Shares: " + currActivity.getObject().getResharers().toPrettyString() + "<br>");
//			out.println("Content: " + currActivity.getObject().getContent() + "<br>");
//						
//			ArrayList<Comment> comments = PlusUtils.getAllComments(currActivity);
//			Iterator<Comment> iterComm = comments.iterator();
//			while (iterComm.hasNext()) {
//				Comment currComm = iterComm.next();
//				out.println("<p>Autore: " + currComm.getActor().getDisplayName() + "<br>");
//				out.println("<p>Plusoners: " + currComm.getPlusoners().size() + "<br>");
//				out.println("<p>Published: " + currComm.getPublished() + "<br>");
//				out.println("<p>Content: " + currComm.getObject().getContent() + "<br>");
//			}
//			
//		}
		
//		resp.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = resp.getWriter();
//		
//		String result = "";
//		
//		ArrayList<String> idSindaci = new ArrayList<String>();
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Query q = new Query("sindaco");
//		PreparedQuery pq = datastore.prepare(q);
//		for (Entity res : pq.asIterable()) {
//			String idPage = res.getKey().getName();
//			idSindaci.add(idPage);
//		}
//		
//		
//		

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
