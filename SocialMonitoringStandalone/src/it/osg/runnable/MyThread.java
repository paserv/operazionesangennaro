package it.osg.runnable;

import it.osg.data.PSAR;
import it.osg.queue.Queue;

public class MyThread implements Runnable {

	private Queue queue;
	private String userID;
	private String postID;
	private PSAR data;


	public MyThread(String userID, String postID, PSAR data) {
		super();
		this.userID = userID;
		this.postID = postID;
		this.data = data;
	}
	
	public void addToQueue (Queue queue) {
		this.queue = queue;
//		queue.addThreadToQueue(this);
	}

	@Override
	public void run() {
		System.out.println("Trying thread: " + postID);
		while (true) {
			if (queue.getActiveThread().get() < queue.getMaxActiveThread()) {
				queue.getActiveThread().incrementAndGet();
				System.out.println("Active Thread: " + queue.getActiveThread());
				this.start();
				break;
			} else {
				try {
					System.out.println("Sleeping thread: " + postID);
					System.out.println("Active Thread: " + queue.getActiveThread());
					Thread.sleep(queue.getThreadSleepInterval());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private void start () {
		
		System.out.println("Starting thread: " + postID);
		System.out.println("Active Thread: " + queue.getActiveThread());
		
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		Post completePost = FacebookUtils.getPost(this.postID);
//
//		int commentNum = 0;
//		int likesNum = 0;
//		int sharesNum = 0;
//		int commentsPageToPagePost = 0;
//
//		int commentToFanPost = 0;
//		int commentPageToFanPost = 0;
//
//		try {
//			if (completePost.getFrom().getId().equals(this.userID) && completePost.getMessage() != null) {
//				
//				data.getPostFromPage().incrementAndGet();
//
//				ArrayList<Comment> commentsList = FacebookUtils.getAllComments(completePost);
//				commentNum = commentsList.size();
//				data.addComments(commentNum);
//
//				ArrayList<Like> likesList = FacebookUtils.getAllLikes(completePost);
//				likesNum = likesList.size();
//				data.addLikes(likesNum);
//
//				sharesNum = FacebookUtils.getSharesInteger(completePost);
//				data.addShares(sharesNum);
//
//				commentsPageToPagePost = FacebookUtils.getCommentsFromIdCountInteger(this.userID, commentsList);
//				data.addCommnetsFromPageToPostFromPage(commentsPageToPagePost);
//
//			} else {
//				data.getPostFromFan().incrementAndGet();
//
//				ArrayList<Comment> commentsToPostFromFanList = FacebookUtils.getAllComments(completePost);
//				commentToFanPost = commentsToPostFromFanList.size();
//				data.addCommentsToPostFromFan(commentToFanPost);
//
//				commentPageToFanPost = FacebookUtils.getCommentsFromIdCountInteger(this.userID, commentsToPostFromFanList);
//				data.addCommnetsFromPageToPostFromFan(commentPageToFanPost);
//
//			}
//		} catch (Exception e) {
//			System.out.println("ERRORE");
//		}
		
		data.getLikes().incrementAndGet();

		System.out.println("Thread terminated: " + postID);
		queue.getActiveThread().decrementAndGet();
		queue.getExecutedThread().incrementAndGet();

	}


}


