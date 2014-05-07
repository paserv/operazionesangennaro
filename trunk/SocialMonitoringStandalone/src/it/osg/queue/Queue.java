package it.osg.queue;

import it.osg.runnable.exception.TimeoutException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue {

	private ArrayList<QueueThread> queue = new ArrayList<QueueThread>();
	private ArrayList<Thread> threads = new ArrayList<Thread>();

	private AtomicInteger activeThread = new AtomicInteger(0);
	private AtomicInteger executedThread = new AtomicInteger(0);
	private int maxActiveThread;

	private long threadSleepInterval;
	private long awaitTerminationSleepInterval;

	private long startTimestamp;

	public Queue(int maxActiveThread, long threadSleepInterval, long queueCheckSleepInterval) {
		this.maxActiveThread = maxActiveThread;
		this.threadSleepInterval = threadSleepInterval;
		this.awaitTerminationSleepInterval = queueCheckSleepInterval;
		
		this.startTimestamp = System.currentTimeMillis();
	}

	public void execute () {
		for (int i = 0; i < queue.size(); i++) {
			QueueThread mt = queue.get(i);
			Thread worker = new Thread(mt);
			threads.add(worker);
			worker.start();
		}
	}

	public void shutDownNow() {
		for (int i = 0; i < threads.size(); i++) {
			Thread currentThread = threads.get(i);
			if (currentThread.isAlive()) {
				try {
					currentThread.join(1L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}
		try {
			Thread.currentThread().join(1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}

	public boolean awaitTermination (long mseconds) throws TimeoutException {
		try {
			Thread.sleep(awaitTerminationSleepInterval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - this.startTimestamp;

		if (elapsedTime >= mseconds) {
			throw new TimeoutException();
		}

		if (this.getActiveThread().intValue() == 0 && this.executedThread.intValue() == this.queue.size()) {
			return true;
		}

		return false;

	}

	public void awaitQueueTermination(long mseconds) {

		while (true) {
			boolean finished;
			try {
				finished = this.awaitTermination(mseconds);
				if (finished) {
					break;
				}
			} catch (TimeoutException e) {
				e.printStackTrace();
				System.out.println("TIMEOUT");
				this.shutDownNow();
				return;
			}
			
		}

	}



	public void addThreadToQueue (QueueThread thread) {
		this.queue.add(thread);
	}

	public AtomicInteger getActiveThread() {
		return activeThread;
	}

	public void setActiveThread(AtomicInteger activeThread) {
		this.activeThread = activeThread;
	}

	public int getMaxActiveThread() {
		return maxActiveThread;
	}

	public void setMaxActiveThread(int maxActiveThread) {
		this.maxActiveThread = maxActiveThread;
	}

	public long getThreadSleepInterval() {
		return threadSleepInterval;
	}

	public void setThreadSleepInterval(long threadSleepInterval) {
		this.threadSleepInterval = threadSleepInterval;
	}

	public AtomicInteger getExecutedThread() {
		return executedThread;
	}

	public void setExecutedThread(AtomicInteger executedThread) {
		this.executedThread = executedThread;
	}

	public ArrayList<QueueThread> getQueue() {
		return queue;
	}

	public void setQueue(ArrayList<QueueThread> queue) {
		this.queue = queue;
	}



}
