package it.osg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

public class FacebookUtils {

	private static String appID= "156346967866710";
	private static String appKey= "e0f880cc248e811c98952d9a44a27ce4";
	private static String accessToken = "156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk";
	
	
	private static Facebook getFB() {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appID, appKey);
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
		
		return facebook;
	}
	
	public static ArrayList<Post> getAllPosts (String pageId, Date f, Date t, String[] campi) {

		ArrayList<Post> result = new ArrayList<Post>();
		
		Facebook facebook = getFB();	
		
		ResponseList<Post> facResults;
		try {
			facResults = facebook.getFeed(pageId, new Reading().since(f).until(t).fields(campi));
			result.addAll(facResults);
			
//			//Fetching Post
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
								if (fetchPost.getCreatedTime().after(f) && fetchPost.getCreatedTime().before(t)) {
									result.add(fetchPost);
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
		
		return result;

	} 
	
	public static ArrayList<Comment> getAllComments(Post curr, Facebook facebook, Date f, Date t) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		if (curr.getCreatedTime().after(f) && curr.getCreatedTime().before(t)) {
			PagableList<Comment> comments = curr.getComments();
			if (comments != null) {
				Iterator<Comment> iterComment = comments.iterator();
				//Salvo i Commenti
				while (iterComment.hasNext()) {
					Comment currComment = iterComment.next();
					if (currComment != null) {
						result.add(currComment);
					}
					//TODO salvare le risposte ai commenti

				}
				//Itero sulla paginazione per salvare tutti i commenti
				Paging<Comment> paging = comments.getPaging();
				while (true) {
					if (paging != null) {
						ResponseList<Comment> nextPage;
						try {
							nextPage = facebook.fetchNext(paging);
							if (nextPage != null) {
								Iterator<Comment> itr = nextPage.iterator();
								while (itr.hasNext()) {
									Comment cmt = itr.next();
									if (cmt != null) {
										result.add(cmt);
									}
								}
							} else {
								break;
							}
							paging = nextPage.getPaging();
						} catch (FacebookException e) {
							e.printStackTrace();
						}
					} else {
						break;
					}

				}
			
			}
		}
		return result;
	}
	
	public static ArrayList<Comment> getComments (ArrayList<Post> posts, Date f, Date t) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		Facebook facebook = getFB();	
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			ArrayList<Comment> currComments = getAllComments(curPost, facebook, f, t);
			result.addAll(currComments);
		}
		return result;
		
	}
	
	public static ArrayList<String> getUniqueAuthors (ArrayList<Comment> comments, Date f, Date t) {
		
		ArrayList<String> result = new ArrayList<String>();
		
		//Per tutti i commenti accumulati prelevo gli autori
		Iterator<Comment> iterComm = comments.iterator();
		while (iterComm.hasNext()) {
			Comment currComm = iterComm.next();
			result.add(currComm.getFrom().getId());
		}
		
		return ArrayUtils.removeDuplicate(result);
	}
			
//	public static ArrayList<String> getPostLikes (ArrayList<Post> posts) {
//		Facebook facebook = new FacebookFactory().getInstance();
//		facebook.setOAuthAppId(appID, appKey);
//		//facebook.setOAuthPermissions(commaSeparetedPermissions);
//		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
//		
//		Iterator 
//		facebook.
//	}
	
}
