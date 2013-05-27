package it.osg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.Question.Option;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken("156346967866710|gnswdSXw_ObP0RaWj5qqgK_HtCk", null));
		
		try {
			ResponseList<Post> results = facebook.getFeed("166115370094396", new Reading().until(new Date(2013, 05 ,25, 8, 00)).since(new Date(2013, 05 ,26, 8, 00)).limit(100));
			Iterator<Post> iter = results.iterator();
			while (iter.hasNext()){
				Post curr = iter.next();
				String currId = curr.getId();
				String test = curr.getMessage();
				out.println("Post: " + test);
				
				PagableList<IdNameEntity> likes = curr.getLikes();
				
				out.println("Likes: " + likes.size());
				
				Iterator<IdNameEntity> iterLikes = likes.iterator();
				while (iterLikes.hasNext()){
					IdNameEntity currNE = iterLikes.next();
					out.println("ID likes: " + currNE.getId());
				}
				
				
			}
			
			
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		

		out.println("CIAO");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	

	
	
	

}
