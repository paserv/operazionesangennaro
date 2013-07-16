package it.osg.servlet;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.MailUtils;

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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(TestServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//log.info("TEST");
		
		resp.setContentType("text/html;charset=UTF-8");
		//PrintWriter out = resp.getWriter();
		
		String result = "";
		
		ArrayList<String> idSindaci = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("sindaco");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity res : pq.asIterable()) {
			String idPage = res.getKey().getName();
			idSindaci.add(idPage);
		}
		
		
		
		Facebook facebook = FacebookUtils.getFB();
		
		Iterator<String> iter = idSindaci.iterator();
		while (iter.hasNext()) {
			Date f = null;
			Date t = null;
			try {
				f = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
				t = DateUtils.addMonthToDate(f, 1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String currId = iter.next();
			try {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					ResponseList<Post> facResults = facebook.getFeed(currId, new Reading().since(f).until(t).fields("created_time").limit(1));
					if (facResults != null && facResults.size() != 0) {
						Post currPost = facResults.get(0);
						System.out.println(currId + "," + DateUtils.formatDateAndTime(currPost.getCreatedTime()));
						result = result + currId + "," + DateUtils.formatDateAndTime(currPost.getCreatedTime()) + "\n";
						//out.println(currId + "," + DateUtils.formatDateAndTime(currPost.getCreatedTime()) +  "<br>");
						break;
					} else {
						f = t;
						t = DateUtils.addMonthToDate(f, 1);
					}
				}
				
			} catch (FacebookException e) {
				e.printStackTrace();
			}
						
		}
		
		MailUtils.sendMail("paserv@gmail.com", "START DATE FACEBOOK ID", result, "result.csv", result);
		
		
//		String strCallResult = "";
//		resp.setContentType("text/plain");
//		try {
//			//Extract out the To, Subject and Body of the Email to be sent
//			//		String strTo = req.getParameter("email_to");
//			//		String strSubject = req.getParameter("email_subject");
//			//		String strBody = req.getParameter("email_body");
//
//			String strTo = "paserv@gmail.com";
//			String strSubject = "test";
//			String strBody = "ciao";
//			byte[] attachmentData = strBody.getBytes();
//
//			//Do validations here. Only basic ones i.e. cannot be null/empty
//			//Currently only checking the To Email field
//			if (strTo == null) throw new Exception("To field cannot be empty.");
//
//			//Trim the stuff
//			strTo = strTo.trim();
//			if (strTo.length() == 0) throw new Exception("To field cannot be empty.");
//
//			//Call the GAEJ Email Service
//			Properties props = new Properties();
//			Session session = Session.getDefaultInstance(props, null);
//			Message msg = new MimeMessage(session);
//			msg.setFrom(new InternetAddress("donpablooooo@gmail.com"));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
//			msg.setSubject(strSubject);
//			//msg.setText(strBody);
//									
//			Multipart mp = new MimeMultipart();
//			MimeBodyPart htmlPart = new MimeBodyPart();
//	        htmlPart.setContent(strBody, "text/plain");
//	        mp.addBodyPart(htmlPart);
//	        
//	        MimeBodyPart attachment = new MimeBodyPart();
//	        ByteArrayDataSource src = new ByteArrayDataSource(attachmentData, "text/plain"); 
//	        attachment.setFileName("ciao.txt");
//	        
//	        attachment.setDataHandler(new DataHandler (src)); 
//	        
//	        //attachment.setContent(attachmentData, "text/plain");
//	        mp.addBodyPart(attachment);
//	        
//	        msg.setContent(mp);
//	        msg.saveChanges();
//
//			Transport.send(msg);
//			strCallResult = "Success: " + "Email has been delivered.";
//			resp.getWriter().println(strCallResult);
//		}
//		catch (Exception ex) {
//			strCallResult = "Fail: " + ex.getMessage();
//			resp.getWriter().println(strCallResult);
//		}


		//		Date f = null;
		//		Date t = null;
		//		try {
		//			f = DateUtils.parseDateAndTime("01-04-2013 00:00:00");
		//			t = DateUtils.parseDateAndTime("30-06-2013 23:59:59");
		//		} catch (ParseException e1) {
		//			e1.printStackTrace();
		//		}
		//				
		//		resp.setContentType("text/html;charset=UTF-8");
		//		PrintWriter out = resp.getWriter();
		//
		//		Facebook facebook = new FacebookFactory().getInstance();
		//		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		//		facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));
		//
		//				
		//		ArrayList<Post> posts = FacebookUtils.getAllPosts("166115370094396", f, t, null);
		//		
		//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//		Entity currEntity = new Entity("queue");
		//		currEntity.setProperty("posts", posts.size());
		//		datastore.put(currEntity);

		//		Iterator<Post> iterPost = posts.iterator();
		//		while (iterPost.hasNext()) {
		//			Post curPost = iterPost.next();
		//			out.println("ID Post = " + curPost.getId() + "<br>");
		//			out.println("Post Message = " + curPost.getMessage() + "<br>");
		//			out.println("Post Shares Count = " + curPost.getSharesCount() + "<br>");
		//			out.println("Post Type = " + curPost.getType() + "<br>");
		//			if (curPost.getFrom() != null) {
		//				out.println("Post FROM Name = " + curPost.getFrom().getName() + "<br>");
		//				out.println("Post FROM ID = " + curPost.getFrom().getId() + "<br>");
		//			}
		//			if (curPost.getTo() != null) {
		//				out.println("Post Type = " + curPost.getTo().toString() + "<br>");
		//			}
		//			
		//			out.println("<br>" + "---------------getAllLikes-------------" + "<br>");
		//			
		//			
		//									
		//			out.println("<br>" + "---------------getAllLikes-------------" + "<br>");
		//			
		//			ArrayList<Like> likes = FacebookUtils.getAllLikes(curPost);
		//			out.println("----> Num Likes = " + likes.size() + "<br>");
		//			Iterator<Like> iter = likes.iterator();
		//			while (iter.hasNext()) {
		//				Like curr = iter.next();
		//				out.println("ID = " + curr.getId() + "<br>");
		//				out.println("NAME = " + curr.getName() + "<br>");
		//			}
		//			
		//		}






		//		ResponseList<Post> facResults;
		//		try {
		//			facResults = facebook.getFeed("Ballaro.Rai", new Reading().since(f).until(t));
		//			Iterator<Post> it = facResults.iterator();
		//			while(it.hasNext()){
		//				Post cu = it.next();
		//				out.println("POST: " + cu.getMessage() + "<br>");
		//				out.println("Type: " + cu.getType() + "<br>");
		//				out.println("Description: " + cu.getDescription() + "<br>");
		//				out.println("SharesCount: " + cu.getSharesCount() + "<br>");
		//				out.println("CreatedTime: " + cu.getCreatedTime() + "<br>");
		//				out.println("From: " + cu.getFrom() + "<br>");
		//				out.println("LikesCount: " + cu.getLikes().size() + "<br>");
		//				out.println("MessageTagsCount: " + cu.getMessageTags() + "<br>");
		//				out.println("Metadata: " + cu.getMetadata() + "<br>");
		//				out.println("Place: " + cu.getPlace() + "<br>");
		//				out.println("UpdateTime: " + cu.getUpdatedTime() + "<br>");				
		//			}			
		//
		//			//Fetching Post
		//			Paging<Post> pagingPost = facResults.getPaging();
		//			while (true) {
		//				if (pagingPost != null) {
		//					ResponseList<Post> nextPosts = facebook.fetchNext(pagingPost);
		//					if (nextPosts != null) {
		//						Post firstPost = nextPosts.get(0);
		//						if (firstPost.getCreatedTime().after(t) || firstPost.getCreatedTime().before(f)) {
		//							break;
		//						}
		//						Iterator<Post> itr = nextPosts.iterator();
		//						while (itr.hasNext()) {
		//							Post fetchPost = itr.next();
		//							if (fetchPost.getCreatedTime().after(f) && fetchPost.getCreatedTime().before(t)) {
		//								out.println("Other POSTS: " + fetchPost.getMessage() + "<br>");
		//								out.println("Other POSTS Created: " + fetchPost.getCreatedTime() + "<br>");
		//							}
		//						}
		//					} else {
		//						break;
		//					}
		//					pagingPost = nextPosts.getPaging();
		//				} else {
		//					break;
		//				}
		//			}
		//		} catch (FacebookException e) {
		//			e.printStackTrace();
		//		}
		//
		//		out.println("<br>FINE");		





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
