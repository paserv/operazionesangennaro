package it.osg.servlet.psar;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class SubTaskServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String idTransaction = req.getParameter("idTransaction");
		//String mail = req.getParameter("mail");
		//String timestamp = req.getParameter("timestamp");
		String pageId = req.getParameter("pageId");
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		
		//OUTPUT DATA
		double totParzPostFromPage = 0;
		double totParzPostFromFan = 0;
		double totParzComments = 0;
		String authors = "";
		double totParzLikes = 0;
		double totParzShares = 0;
		
				
		//GET DATE
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(from);
			t = DateUtils.parseDateAndTime(to);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
				
		//Get all Post
		ArrayList<Post> posts = FacebookUtils.getAllPosts(pageId, f, t, null);
		
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
		
		totParzPostFromPage = postFromPage.size();
		totParzPostFromFan = postFromFan.size();
		
		//Get all Comments
		ArrayList<Comment> comments = FacebookUtils.getComments(postFromPage);
		totParzComments = comments.size();
		
		//Get all Unique Authors of Comments
		ArrayList<String> uniqueAuth = FacebookUtils.getUniqueAuthors(comments);
		Iterator<String> iter = uniqueAuth.iterator();
		while (iter.hasNext()) {
			authors = authors + iter.next() + ",";
		}
		
		//Get all Likes to Posts
		ArrayList<Like> likes = FacebookUtils.getLikes(postFromPage);
		totParzLikes = likes.size();
		
		//Get all Shares to Posts
		totParzShares = FacebookUtils.getShares(postFromPage);
		
		
		//SAVE OUTPUT TO DATASTORE
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity currEntity = new Entity("task");
		currEntity.setProperty("idTransaction", idTransaction);
		currEntity.setProperty("pageId", pageId);
		//currEntity.setProperty("from", f);
		//currEntity.setProperty("to", t);
		//currEntity.setProperty("mail", mail);
		//currEntity.setProperty("timestamp", Long.valueOf(timestamp));
		
		currEntity.setProperty("totParzPostFromPage", totParzPostFromPage);
		currEntity.setProperty("totParzPostFromFan", totParzPostFromFan);
		currEntity.setProperty("totParzComments", totParzComments);
		currEntity.setProperty("authors", new Text(authors));
		currEntity.setProperty("totParzLikes", totParzLikes);
		currEntity.setProperty("totParzShares", totParzShares);

		datastore.put(currEntity);
		

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	

}
