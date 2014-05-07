package it.osg.queue;

import java.util.ArrayList;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.utils.FacebookUtils;

public class QueueThreadImpl extends QueueThread {

	private String userID;
	private String postID;
	private PSAR data;


	public QueueThreadImpl(String userID, String postID, PSAR data) {
		super();
		this.userID = userID;
		this.postID = postID;
		this.data = data;
	}
	

	public void start () {
		
		Post completePost = FacebookUtils.getPost(this.postID);

		int commentNum = 0;
		int likesNum = 0;
		int sharesNum = 0;
		int commentsPageToPagePost = 0;

		int commentToFanPost = 0;
		int commentPageToFanPost = 0;

		try {
			if (completePost.getFrom().getId().equals(this.userID) && completePost.getMessage() != null) {
				
				data.getPostFromPage().incrementAndGet();

				ArrayList<Comment> commentsList = FacebookUtils.getAllComments(completePost);
				commentNum = commentsList.size();
				data.addComments(commentNum);

				ArrayList<Like> likesList = FacebookUtils.getAllLikes(completePost);
				likesNum = likesList.size();
				data.addLikes(likesNum);

				sharesNum = FacebookUtils.getSharesInteger(completePost);
				data.addShares(sharesNum);

				commentsPageToPagePost = FacebookUtils.getCommentsFromIdCountInteger(this.userID, commentsList);
				data.addCommnetsFromPageToPostFromPage(commentsPageToPagePost);

			} else {
				data.getPostFromFan().incrementAndGet();

				ArrayList<Comment> commentsToPostFromFanList = FacebookUtils.getAllComments(completePost);
				commentToFanPost = commentsToPostFromFanList.size();
				data.addCommentsToPostFromFan(commentToFanPost);

				commentPageToFanPost = FacebookUtils.getCommentsFromIdCountInteger(this.userID, commentsToPostFromFanList);
				data.addCommnetsFromPageToPostFromFan(commentPageToFanPost);

			}
		} catch (Exception e) {
			System.out.println("ERRORE");
		}

	}


}


