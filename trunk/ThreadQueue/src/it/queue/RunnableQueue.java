package it.queue;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class RunnableQueue implements Runnable {
	
	private static Logger LOGGER = Logger.getLogger(RunnableQueue.class.getName());

	public String name;
	private Queue queue;
	public abstract void executeJob() throws Exception;
	public abstract void rollback();
	
	public RunnableQueue() {
		Handler handler = new ConsoleHandler();
		Formatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		LOGGER.addHandler(handler);
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
	

	@Override
	public void run() {
		try {
			LOGGER.fine("Executing Job for Thread: " + this.getName());
			this.executeJob();
			LOGGER.fine("Decrement Running Thread:" + this.getName());
			this.queue.getCounter().runningThread.decrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Rolling Back Thread: " + this.getName());
			this.rollback();
			LOGGER.severe("Enqueue Failed Thread " + this.getName());
			this.queue.addThread(this);
			this.queue.getCounter().runningThread.decrementAndGet();
//			return;
		}
		
	}
	public static Logger getLOGGER() {
		return LOGGER;
	}
	public static void setLOGGER(Logger lOGGER) {
		LOGGER = lOGGER;
	}
	
}
