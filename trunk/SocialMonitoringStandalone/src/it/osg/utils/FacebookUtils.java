package it.osg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Like;
import facebook4j.PagableList;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FacebookUtils {

	private static ArrayList<Facebook> facebookPool = null;
	private static int nextFBCredential = 0;

	public static Facebook getFB() {
		if (facebookPool != null) {
			if ((nextFBCredential + 1) == Constants.FACEBOOK_CREDENTIAL_VECTOR.size()) {
				nextFBCredential = 0;
			} else {
				nextFBCredential++;
			}
			return facebookPool.get(nextFBCredential);
		}

		facebookPool = new ArrayList<Facebook>();
		for (int i = 0; i < Constants.FACEBOOK_CREDENTIAL_VECTOR.size(); i++) {
			Facebook fac = new FacebookFactory().getInstance();
			fac.setOAuthAppId(Constants.FACEBOOK_CREDENTIAL_VECTOR.get(i).getAppId(), Constants.FACEBOOK_CREDENTIAL_VECTOR.get(i).getAppKey());
			//facebook.setOAuthPermissions(commaSeparetedPermissions);
			fac.setOAuthAccessToken(new AccessToken(Constants.FACEBOOK_CREDENTIAL_VECTOR.get(i).getAppAccessToken(), null));
			facebookPool.add(fac);
		}

		return facebookPool.get(nextFBCredential);
	}

	public static void main(String[] args) {
		Post ps = getPost("179618821150_10152142823961151");
		System.out.println("jj");
	}


	public static Date getActivityInterval (String pageId){

		Facebook facebook = getFB();
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
			t = DateUtils.addMonthToDate(f, 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				ResponseList<Post> facResults = facebook.getFeed(pageId, new Reading().since(f).until(t).fields("created_time").limit(1));
				if (facResults != null && facResults.size() != 0) {
					Post currPost = facResults.get(0);
					return currPost.getCreatedTime();
				} else {
					f = t;
					t = DateUtils.addMonthToDate(f, 1);
				}
			}

		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return null;

	}

	//	public static Hashtable<String, Object> getBaseInfo (String pageId) {
	//
	//		Random rand = new Random();
	//		int seconds = rand.nextInt(4000) + 1;
	//		
	//		try {
	//			Thread.sleep(seconds);
	//		} catch (InterruptedException e1) {
	//			e1.printStackTrace();
	//		}
	//		
	//		Facebook facebook = getFB();
	//		Hashtable<String, Object> result = new Hashtable<String, Object>();
	//		
	//		try {
	//			Page page = facebook.getPage(pageId);
	//			String likeCount = String.valueOf(page.getLikes());
	//			String talkingAboutCount = String.valueOf(page.getTalkingAboutCount());
	//			String pageName = String.valueOf(page.getName());
	//			//TODO retrieve anche degli altri campi
	//
	//			result.put("likes", likeCount);
	//			result.put("talking_about_count", talkingAboutCount);
	//			result.put("name", pageName);
	//		} catch (FacebookException e) {
	//			e.printStackTrace();
	//		}
	//		
	//		return result;
	//	}

	public static Hashtable<String, Object> getBaseInfoFromJson (String pageId) {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		String jsonString = JSONObjectUtil.retrieveJson(Constants.FACEBOOK_GRAPH_API_ROOT_URL + pageId);
		//		DatastoreUtils.addRow("test", "json", new Text(jsonString));

		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;

		String likeCount = JSONObjectUtil.retrieveJsonPath(json, "likes");
		String talkingAboutCount = JSONObjectUtil.retrieveJsonPath(json, "talking_about_count");
		String pageName = JSONObjectUtil.retrieveJsonPath(json, "name");
		//TODO retrieve anche degli altri campi

		result.put("likes", likeCount);
		result.put("talking_about_count", talkingAboutCount);
		result.put("name", pageName);

		return result;
	}


	public static ArrayList<Post> getAllOwnPosts (String pageId, Date f, Date t, String[] campi) {
		ArrayList<Post> result = new ArrayList<Post>();
		ArrayList<Post> posts = getAllPosts(pageId, f, t, campi);
		Iterator<Post> iter = posts.iterator();
		while (iter.hasNext()) {
			Post currPost = iter.next();
			if (currPost.getFrom().getId().toString().equalsIgnoreCase(pageId)){
				result.add(currPost);
			}
		}
		return result;
	}

	public static Post getPost (String postId) {
		Facebook facebook = getFB();
		Post result = null;
		try {
			result = facebook.getPost(postId);
		} catch (FacebookException e) {
			e.printStackTrace();
		}
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
					if (nextPosts != null && nextPosts.size() > 0) {
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

	public static long getShares (ArrayList<Post> posts) {
		long result = 0;
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			if (curPost.getSharesCount() != null) {
				result = result + curPost.getSharesCount();
			}
		}
		return result;

	}

	public static int getSharesInteger (ArrayList<Post> posts) {
		int result = 0;
		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post curPost = iterPost.next();
			if (curPost.getSharesCount() != null) {
				result = result + curPost.getSharesCount();
			}
		}
		return result;

	}

	public static int getSharesInteger (Post post) {
		if (post.getSharesCount() != null) return post.getSharesCount();
		return 0;

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

	public static long getCommentsFromIdCount (String id, ArrayList<Comment> comments) {

		long result = 0;

		//Per tutti i commenti accumulati prelevo gli autori
		Iterator<Comment> iterComm = comments.iterator();
		while (iterComm.hasNext()) {
			Comment currComm = iterComm.next();
			if (currComm.getFrom().getId().equalsIgnoreCase(id)) {
				result++;
			}

		}

		return result;
	}



	public static int getCommentsFromIdCountInteger (String id, ArrayList<Comment> comments) {

		int result = 0;

		//Per tutti i commenti accumulati prelevo gli autori
		Iterator<Comment> iterComm = comments.iterator();
		while (iterComm.hasNext()) {
			Comment currComm = iterComm.next();
			if (currComm.getFrom().getId().equalsIgnoreCase(id)) {
				result++;
			}

		}

		return result;
	}


	public static ArrayList<Hashtable<String, Object>> likeTalkAnalysis(String jsonString) {
		ArrayList<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;

		Hashtable<String, Object> currRow = new Hashtable<String, Object>();

		String likeCount = JSONObjectUtil.retrieveJsonPath(json, "likes");
		String talkingAboutCount = JSONObjectUtil.retrieveJsonPath(json, "talking_about_count");

		System.out.println(likeCount + " " + talkingAboutCount);

		if (!likeCount.equalsIgnoreCase("")) {
			currRow.put("like_count", Integer.parseInt(likeCount));
		}

		if (!talkingAboutCount.equalsIgnoreCase("")) {
			currRow.put("talking_about_count", Integer.parseInt(talkingAboutCount));
		}

		Calendar cal= Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.HOUR_OF_DAY, 2);
		SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date cestDate= cal.getTime();

		currRow.put("timestamp", cal.getTimeInMillis());
		currRow.put("date", cestDate);

		//System.out.println(cal.getTimeInMillis() + " " + sdf.format(cestDate));

		result.add(currRow);

		return result;
	}

	public static ArrayList<Hashtable<String, String>> analyse(String jsonString) {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;
		JSONArray dataArray = (JSONArray) json.get("data");
		if (dataArray != null){
			for (int i = 0; i < dataArray.size(); i++) {
				Hashtable<String, String> currRow = new Hashtable<String, String>();
				currRow.put("timestamp", String.valueOf(System.currentTimeMillis()));
				JSONObject currJson =(JSONObject) dataArray.get(i);
				String postId = JSONObjectUtil.retrieveJsonPath(currJson, "id");
				currRow.put("postId", postId);
				String object_id = JSONObjectUtil.retrieveJsonPath(currJson, "object_id");
				currRow.put("postId", object_id);
				String created_time = JSONObjectUtil.retrieveJsonPath(currJson, "created_time");
				currRow.put("created_time", created_time);
				String updated_time = JSONObjectUtil.retrieveJsonPath(currJson, "updated_time");
				currRow.put("updated_time", updated_time);
				JSONObject currComments = (JSONObject) currJson.get("comments");
				if (currComments != null){
					JSONArray currCommentsArray = (JSONArray) currComments.get("data");
					String comment_number = String.valueOf(currCommentsArray.size());
					currRow.put("comment_number", comment_number);
					int post_likes = Integer.parseInt(JSONObjectUtil.retrieveJsonPath(currJson, "likes/count"));
					currRow.put("post_likes", String.valueOf(post_likes));
					int comments_like = 0;
					for (int j = 0; j < currCommentsArray.size(); j++) {
						JSONObject currJsonComment =(JSONObject) currCommentsArray.get(j);
						String like_count = JSONObjectUtil.retrieveJsonPath(currJsonComment, "like_count");;
						int currCommLike = Integer.parseInt(like_count);
						comments_like = comments_like + currCommLike;
					}
					String lik = String.valueOf(comments_like);
					currRow.put("comments_like", lik);
				}
				result.add(currRow);
			}

		}


		return result;
	}

}
