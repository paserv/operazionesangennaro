package it.osg.runnable;

import java.util.ArrayList;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.utils.FacebookUtils;
import it.queue.RunnableQueue;

public class RunnableQueueImpl extends RunnableQueue {

	private String userID;
	private String postID;
	private PSAR data;

	public RunnableQueueImpl(String userID, String postID, PSAR data) {
		super();
		this.userID = userID;
		this.postID = postID;
		this.data = data;
	}

	@Override
	public void executeJob() throws Exception {
		Post completePost = FacebookUtils.getPost(this.postID);

		int commentNum = 0;
		int likesNum = 0;
		int sharesNum = 0;
		int commentsPageToPagePost = 0;

		int commentToFanPost = 0;
		int commentPageToFanPost = 0;

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


	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
