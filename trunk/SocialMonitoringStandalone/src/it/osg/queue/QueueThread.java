package it.osg.queue;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class QueueThread implements Runnable {

	protected Queue queue;
	protected String name = "";
	private static Logger LOGGER = Logger.getLogger(QueueThread.class.getName());
	
	private static Random rand = new Random();
	
	public abstract void start();
	
	public void addToQueue (Queue queue) {
		this.queue = queue;
		queue.addThreadToQueue(this);
		//ConsoleHandler ch = new ConsoleHandler();
		LOGGER.setLevel(Level.INFO);
	}
	
	
	@Override
	public void run() {
		while (true) {
			LOGGER.info("Active Thread: " + queue.getActiveThread());
			if (queue.getActiveThread().get() < queue.getMaxActiveThread()) {
				queue.getActiveThread().incrementAndGet();
				LOGGER.info("Starting Thread: " + this.getName());
				this.start();
				LOGGER.info("End Thread: " + this.getName());
				queue.getActiveThread().decrementAndGet();
				queue.getExecutedThread().incrementAndGet();
				break;
			} else {
				try {
					long interval = 0L + (long) (rand.nextDouble()*(queue.getThreadSleepInterval()));
					LOGGER.info("Waiting " + interval + " ms" + ": too many running threads - " + queue.getActiveThread());
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
