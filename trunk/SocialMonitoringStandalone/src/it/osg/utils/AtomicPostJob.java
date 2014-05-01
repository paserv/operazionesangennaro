package it.osg.utils;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.utils.FacebookUtils;

import java.util.ArrayList;

public class AtomicPostJob implements Runnable {

	private String userID;
	private String postID;
	private PSAR data;
	
	public AtomicPostJob (String userID, String postID, PSAR data) {
		this.userID = userID;
		this.postID = postID;
		this.data = data;
	}
	
	@Override
	public void run() {
		
		System.out.println("Running Post ID: " + postID);
		Post completePost = FacebookUtils.getPost(postID);
		
		if (completePost.getFrom().getId().equals(userID) && completePost.getMessage() != null) {
			data.getPostFromPage().incrementAndGet();
			ArrayList<Comment> commentsList = FacebookUtils.getAllComments(completePost);
			data.addComments(commentsList.size());
			ArrayList<Like> likesList = FacebookUtils.getAllLikes(completePost);
			data.addLikes(likesList.size());
			data.addShares(FacebookUtils.getSharesInteger(completePost));
			data.addCommnetsFromPageToPostFromPage(FacebookUtils.getCommentsFromIdCountInteger(userID, commentsList));
		} else {
			data.getPostFromFan().incrementAndGet();
			ArrayList<Comment> commentsToPostFromFanList = FacebookUtils.getAllComments(completePost);
			data.addCommentsToPostFromFan(commentsToPostFromFanList.size());
			data.addCommnetsFromPageToPostFromFan(FacebookUtils.getCommentsFromIdCountInteger(userID, commentsToPostFromFanList));
		}
		
	}

}
