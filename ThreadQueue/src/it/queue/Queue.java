package it.queue;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Queue {

	private static Logger LOGGER = Logger.getLogger(Queue.class.getName());
	
	private SyncCounter counter = new SyncCounter();
	
	private Hashtable<String, RunnableQueue> threads;
	private int queueLenght = 0;
	
	private long checkSleep;
	private long queueTimeout;
	private long startTime;
	
	
	
	public Queue (int lenght, long checkSleep, long queueTimeout) {
		this.threads = new Hashtable<String, RunnableQueue>();
		this.queueLenght = lenght;
		this.checkSleep = checkSleep;
		this.queueTimeout = queueTimeout;
		Handler handler = new ConsoleHandler();
		Formatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		LOGGER.addHandler(handler);
	}
	
	public synchronized void addThread (RunnableQueue thread) {
		thread.setQueue(this);
		this.threads.put(String.valueOf(this.counter.idIncrement.incrementAndGet()), thread);
		LOGGER.info("Queue lenght: " + this.threads.size());
	}
	
	public boolean executeAndWait() throws TimeoutException {
		
		this.startTime = System.currentTimeMillis();
		while (true) {
			if (this.threads.isEmpty() && this.counter.runningThread.intValue() == 0) {
				LOGGER.info("Queue executed correctly");
				return true;
			}
			long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - startTime;
			if (elapsedTime > this.queueTimeout) {
				LOGGER.severe("Queue in timeout");
				throw new TimeoutException();
			}
			this.run();
			try {
				Thread.sleep(checkSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void run() {
		Enumeration<String> keys = this.threads.keys();
		while (keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			RunnableQueue current = this.threads.get(currKey);
			if (this.counter.runningThread.intValue() < this.queueLenght) {
				Thread worker = new Thread(current);
				if (current.getName() != null) {
					worker.setName(current.getName());
				}
				worker.start();
				LOGGER.info("New thread running: " + worker.getName());
				int runThread = this.counter.runningThread.incrementAndGet();
				LOGGER.info("Running " + runThread + " threads");
				this.threads.remove(currKey);
				LOGGER.info(this.threads.size() + " threads in queue");
			}
		}
	}


	public Hashtable<String, RunnableQueue> getThreads() {
		return threads;
	}

	public void setThreads(Hashtable<String, RunnableQueue> threads) {
		this.threads = threads;
	}

	public SyncCounter getCounter() {
		return counter;
	}

	public void setCounter(SyncCounter counter) {
		this.counter = counter;
	}
	
	
	
}
