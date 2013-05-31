package it.osg.servlet;

import it.osg.utils.DateUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime("01-05-2013 00:00:00");
			t = DateUtils.parseDateAndTime("30-05-2013 00:00:00");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));

		ResponseList<Post> facResults;
		try {
			facResults = facebook.getFeed("Ballaro.Rai", new Reading().since(f).until(t));
			Iterator<Post> it = facResults.iterator();
			while(it.hasNext()){
				Post cu = it.next();
				out.println("POST: " + cu.getMessage() + "<br>");
				out.println("Type: " + cu.getType() + "<br>");
				out.println("Description: " + cu.getDescription() + "<br>");
				out.println("SharesCount: " + cu.getSharesCount() + "<br>");
				out.println("CreatedTime: " + cu.getCreatedTime() + "<br>");
				out.println("From: " + cu.getFrom() + "<br>");
				out.println("LikesCount: " + cu.getLikes().size() + "<br>");
				out.println("MessageTagsCount: " + cu.getMessageTags() + "<br>");
				out.println("Metadata: " + cu.getMetadata() + "<br>");
				out.println("Place: " + cu.getPlace() + "<br>");
				out.println("UpdateTime: " + cu.getUpdatedTime() + "<br>");				
			}			

			//Fetching Post
			Paging<Post> pagingPost = facResults.getPaging();
			while (true) {
				if (pagingPost != null) {
					ResponseList<Post> nextPosts = facebook.fetchNext(pagingPost);
					if (nextPosts != null) {
						Post firstPost = nextPosts.get(0);
						if (firstPost.getCreatedTime().after(t) || firstPost.getCreatedTime().before(f)) {
							break;
						}
						Iterator<Post> itr = nextPosts.iterator();
						while (itr.hasNext()) {
							Post fetchPost = itr.next();
							if (fetchPost.getCreatedTime().after(t) || fetchPost.getCreatedTime().before(f)) {
								out.println("Other POSTS: " + fetchPost.getMessage() + "<br>");
								out.println("Other POSTS Created: " + fetchPost.getCreatedTime() + "<br>");
							}
						}
					} else {
						break;
					}
					pagingPost = nextPosts.getPaging();
				} else {
					break;
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		out.println("<br>FINE");		





		//		Facebook facebook = new FacebookFactory().getInstance();
		//		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		//		facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));
		//		int postCounter = 0;
		//		int commentCounter = 0;
		//		int commentCounterNext = 0;
		//		try {
		//			ResponseList<Post> facResults = facebook.getFeed("Ballaro.Rai", new Reading().since("yesterday"));
		//			Iterator<Post> iter = facResults.iterator();
		//			while (iter.hasNext()){
		//				Post curr = iter.next();
		//				String currText = curr.getMessage();
		//				if (currText != null) {
		//					out.println("POST MESSAGE: " + currText + "<br>");
		//					postCounter++;
		//				}
		//				
		//				PagableList<Comment> comments = curr.getComments();
		//				Iterator<Comment> iterComment = comments.iterator();
		//				while (iterComment.hasNext()) {
		//					Comment currComment = iterComment.next();
		//					String currCommentMessage = currComment.getMessage();
		//					if (currCommentMessage != null) {
		//						out.println("COMMENT: " + currCommentMessage + "<br>");
		//						commentCounter++;
		//					}
		//					
		//					
		//				}
		//				Paging<Comment> paging = comments.getPaging();
		//				while (true) {
		//					if (paging != null) {
		//						ResponseList<Comment> nextPage = facebook.fetchNext(paging);
		//							if (nextPage != null) {
		//								Iterator<Comment> itr = nextPage.iterator();
		//								while (itr.hasNext()) {
		//									Comment cmt = itr.next();
		//									String curCmt = cmt.getMessage();
		//									if (curCmt != null) {
		//										out.println("FETCH COMMENT: " + curCmt + "<br>");
		//										System.out.println(curCmt);
		//										commentCounterNext++;
		//									}
		//								}
		//							} else {
		//								break;
		//							}
		//							paging = nextPage.getPaging();
		//					} else {
		//						break;
		//					}
		//					
		//					
		//					
		//				}
		//								
		//			}
		//			
		//			
		//		} catch (FacebookException e) {
		//			e.printStackTrace();
		//		}
		//		
		//
		//		out.println("POST COUNTER: " + postCounter + "<br>" + " COMMENT COUNTER: " + commentCounter + "<br>" + " PAGE 2 COMMENT: " + commentCounterNext + "<br>");
	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
