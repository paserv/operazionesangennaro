package it.osg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class FacebookUtils {

	public static ArrayList<Post> getAllPosts (Facebook facebook, String pageId, Date f, Date t) {

		ArrayList<Post> result = new ArrayList<Post>();
		
		ResponseList<Post> facResults;
		try {
			facResults = facebook.getFeed(pageId, new Reading().since(f).until(t).limit(1000000));
			result.addAll(facResults);
			
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
//								Post fetchPost = itr.next();
//								if (fetchPost.getCreatedTime().after(t) || fetchPost.getCreatedTime().before(f)) {
//									result.add(fetchPost);
//								}
//						}
//					} else {
//						break;
//					}
//					pagingPost = nextPosts.getPaging();
//				} else {
//					break;
//				}
//			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		
		return result;

	} 
			

	
}
