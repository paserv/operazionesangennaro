package it.osg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FacebookUtils {

	private static String appID= "156346967866710";
	private static String appKey= "e0f880cc248e811c98952d9a44a27ce4";
	private static String accessToken = "156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk";
	
	
	public static ArrayList<Post> getAllPosts (String pageId, Date f, Date t, String[] campi) {

		ArrayList<Post> result = new ArrayList<Post>();
		
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appID, appKey);
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));		
		
		
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
			

	
}
