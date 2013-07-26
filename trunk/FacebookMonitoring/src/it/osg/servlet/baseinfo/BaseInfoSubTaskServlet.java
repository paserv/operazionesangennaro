package it.osg.servlet.baseinfo;

import facebook4j.Post;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.MailUtils;
import it.osg.utils.ShardedCounter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseInfoSubTaskServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String idTransaction = req.getParameter("idTransaction");
		String pageId = req.getParameter("pageId");
		String from = req.getParameter("from");
		String to = req.getParameter("to");

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
		ArrayList<Post> posts = FacebookUtils.getAllPosts(pageId, f, t, new String[]{"id", "from"});
		
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

		//ArrayList<Like> parzLikes = FacebookUtils.getLikes(postFromPage);
		//parzShares = parzShares + FacebookUtils.getShares(postFromPage);
		//ArrayList<Comment> commentsPostFromPage = FacebookUtils.getComments(postFromPage);
		
		String attachFile = postFromFan.size() + "," + postFromPage.size();
		
		MailUtils.sendMail("paserv@gmail.com", "test", "ciao", "test.csv", attachFile);
		
		//SAVE DATA TO DATASTORE AND INCREMENT 1 TASK
		//TODO
		ShardedCounter counter = new ShardedCounter();
		for (int i = 1; i <= 100; i++) {
			counter.increment(idTransaction);	
		}
			
	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
