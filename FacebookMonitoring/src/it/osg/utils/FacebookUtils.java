package it.osg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import facebook4j.Like;
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

	private static String graphAPIUrl = "https://graph.facebook.com/";


	private static Facebook getFB() {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appID, appKey);
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));

		return facebook;
	}

	public static Hashtable<String, Object> getBaseInfo (String pageId) {

		String jsonString = JSONObjectUtil.retrieveJson(graphAPIUrl + pageId);

		Hashtable<String, Object> result = new Hashtable<String, Object>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;

		String likeCount = JSONObjectUtil.retrieveJsonPath(json, "likes");
		String talkingAboutCount = JSONObjectUtil.retrieveJsonPath(json, "talking_about_count");
		String pageName = JSONObjectUtil.retrieveJsonPath(json, "name");
		//TODO retrieve anche degli altri campi

		result.put("likes", likeCount);
		result.put("talking_about_count", talkingAboutCount);
		result.put("pageName", pageName);

		return result;
	}

	public static ArrayList<Post> getAllPosts (String pageId, Date f, Date t, String[] campi) {

		ArrayList<Post> result = new ArrayList<Post>();

		Facebook facebook = getFB();	

		ResponseList<Post> facResults;
		try {
			if (campi != null) {
				facResults = facebook.getFeed(pageId, new Reading().since(f).until(t).fields(campi));
			} else {
				facResults = facebook.getFeed(pageId, new Reading().since(f).until(t));
			}

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

	public static ArrayList<Comment> getAllComments(Post curr) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		Facebook facebook = getFB();

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

		return result;
	}

	public static ArrayList<Like> getAllLikes (Post post) {
		ArrayList<Like> result = new ArrayList<Like>();
		Facebook facebook = getFB();
		ResponseList<Like> likes;
		try {
			likes = facebook.getPostLikes(post.getId());
			if (likes != null)  {
				result.addAll(likes);
			}
			Paging<Like> paging = likes.getPaging();
			while (true) {
				if (paging != null) {
					ResponseList<Like> nextPage;
					try {
						nextPage = facebook.fetchNext(paging);
						if (nextPage != null) {
							Iterator<Like> itr = nextPage.iterator();
							while (itr.hasNext()) {
								Like cmt = itr.next();
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

		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static ArrayList<Comment> getComments (ArrayList<Post> posts) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			ArrayList<Comment> currComments = getAllComments(curPost);
			result.addAll(currComments);
		}
		return result;

	}

	public static ArrayList<Like> getLikes (ArrayList<Post> posts) {
		ArrayList<Like> result = new ArrayList<Like>();
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			ArrayList<Like> currLikes = getAllLikes(curPost);
			result.addAll(currLikes);
		}
		return result;

	}

	public static double getShares (ArrayList<Post> posts) {
		double result = 0;
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			if (curPost.getSharesCount() != null) {
				result = result + curPost.getSharesCount();
			}
		}
		return result;

	}

	public static double getCommentLike (ArrayList<Comment> comments) {
		double result = 0;
		Iterator<Comment> iterComm = comments.iterator();
		while (iterComm.hasNext()) {
			Comment curComm = iterComm.next();
			result = result + curComm.getLikeCount();
		}
		return result;

	}

	public static ArrayList<String> getUniqueAuthors (ArrayList<Comment> comments) {

		ArrayList<String> result = new ArrayList<String>();

		//Per tutti i commenti accumulati prelevo gli autori
		Iterator<Comment> iterComm = comments.iterator();
		while (iterComm.hasNext()) {
			Comment currComm = iterComm.next();
			result.add(currComm.getFrom().getId());
		}

		return ArrayUtils.removeDuplicate(result);
	}



}
