package test;

import java.util.logging.Level;
import it.queue.Queue;
import it.queue.QueueLogger;
import it.queue.TimeoutException;

public class Test {

	public static void main(String[] args) {

		QueueLogger.setup(Level.FINE);
		
		SimpleSyncCounter count = new SimpleSyncCounter();
		Queue queue = new Queue(10, 1000L, 100000L);
				
		for (int i = 0; i < 100; i++) {
			RunnableQueueImpl cur = new  RunnableQueueImpl(count);
			cur.setName("ASSA");
			queue.addThread(cur);
		}
		try {
			queue.executeAndWait();
			
			System.out.println(count.count);
			
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
