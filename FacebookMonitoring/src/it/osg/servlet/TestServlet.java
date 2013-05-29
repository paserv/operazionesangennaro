package it.osg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
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
		facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));
		int postCounter = 0;
		int commentCounter = 0;
		int commentCounterNext = 0;
		try {
			ResponseList<Post> facResults = facebook.getFeed("Ballaro.Rai", new Reading().since("yesterday"));
			Iterator<Post> iter = facResults.iterator();
			while (iter.hasNext()){
				Post curr = iter.next();
				String currText = curr.getMessage();
				if (currText != null) {
					out.println("POST MESSAGE: " + currText + "<br>");
					postCounter++;
				}
				
				PagableList<Comment> comments = curr.getComments();
				Iterator<Comment> iterComment = comments.iterator();
				while (iterComment.hasNext()) {
					Comment currComment = iterComment.next();
					String currCommentMessage = currComment.getMessage();
					if (currCommentMessage != null) {
						out.println("COMMENT: " + currCommentMessage + "<br>");
						commentCounter++;
					}
					
					
				}
				Paging<Comment> paging = comments.getPaging();
				while (true) {
					if (paging != null) {
						ResponseList<Comment> nextPage = facebook.fetchNext(paging);
							if (nextPage != null) {
								Iterator<Comment> itr = nextPage.iterator();
								while (itr.hasNext()) {
									Comment cmt = itr.next();
									String curCmt = cmt.getMessage();
									if (curCmt != null) {
										out.println("FETCH COMMENT: " + curCmt + "<br>");
										System.out.println(curCmt);
										commentCounterNext++;
									}
								}
							} else {
								break;
							}
							paging = nextPage.getPaging();
					} else {
						break;
					}
					
					
					
				}
								
			}
			
			
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		

		out.println("POST COUNTER: " + postCounter + "<br>" + " COMMENT COUNTER: " + commentCounter + "<br>" + " PAGE 2 COMMENT: " + commentCounterNext + "<br>");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	

}
