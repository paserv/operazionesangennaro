package it.osg.utils;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class AtomicJob implements Runnable {

	private String id;
	private String from;
	private String to;
	private PSAR data;
	
	public AtomicJob (String id, String from, String to, PSAR data) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.data = data;
	}
	
	@Override
	public void run() {
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(this.from);
			t = DateUtils.parseDateAndTime(this.to);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//Get all Post
		ArrayList<Post> posts = new ArrayList<Post>();
		posts =	FacebookUtils.getAllPosts(id, f, t, null);

		ArrayList<Post> postFromPageList = new ArrayList<Post>();
		ArrayList<Post> postFromFanList = new ArrayList<Post>();

		Iterator<Post> iterPost = posts.iterator();
		while (iterPost.hasNext()) {
			Post currPost = iterPost.next();
			if (currPost.getFrom().getId().equals(id) && currPost.getMessage() != null) {
				postFromPageList.add(currPost);
			} else {
				postFromFanList.add(currPost);
			}
		}


		//P^{x}[i,j] + S^{x}[i,j] Link, Status, Photo, Video from page 
		data.addPostFromPage(postFromPageList.size());
		data.addPostFromFan(postFromFanList.size());

		//Comments to Post from page
		ArrayList<Comment> commentsList = FacebookUtils.getComments(postFromPageList);
		//C^{x}_{Post^{x}}[i,j] 
		data.addComments(commentsList.size());
		
		//Get all Comments from FAN
		ArrayList<Comment> commentsToPostFromFanList = FacebookUtils.getComments(postFromFanList);
		data.addCommentsToPostFromFan(commentsToPostFromFanList.size());

		//Get all Likes to Posts
		ArrayList<Like> likesList = FacebookUtils.getLikes(postFromPageList);
		//L_{P^{x}}[i,j] 
		data.addLikes(likesList.size());
		
		//S_{P^{x}}[i,j] Get all Shares to Posts
		data.addShares(FacebookUtils.getSharesInteger(postFromPageList));
		
		//C^{x}_{Post^{x}}[i,j]		
		data.addCommnetsFromPageToPostFromPage(FacebookUtils.getCommentsFromIdCountInteger(id, commentsList));
		//C_{Post^{z\neq x}}^{x}[i,j]
		data.addCommnetsFromPageToPostFromFan(FacebookUtils.getCommentsFromIdCountInteger(id, commentsToPostFromFanList));
		
	}

}
