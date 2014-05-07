package it.osg.queue;

import it.osg.data.PSAR;

public class QueueThreadImpl2 extends QueueThread {

	private PSAR data;


	public QueueThreadImpl2(String userID, String postID, PSAR data) {
		super();
		this.data = data;
	}
	

	public void start () {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		data.getLikes().incrementAndGet();
		
	}


}


