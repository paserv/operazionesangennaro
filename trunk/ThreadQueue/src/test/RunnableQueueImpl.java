package test;

import it.queue.RunnableQueue;

public class RunnableQueueImpl extends RunnableQueue {

	private SimpleSyncCounter count;
	private boolean incrementExecuted;
	
	public RunnableQueueImpl(SimpleSyncCounter count) {
		super();
		this.count = count;
	}
	
	@Override
	public void executeJob() throws Exception {
		Thread.sleep(1000L);
		this.count.count.incrementAndGet();
		incrementExecuted = true;
		if (this.count.count.intValue() == 1) {
			this.getLOGGER().fine("Abdulla");
			System.out.println("Eccezione");
			throw new Exception();
		}
		if (this.count.count.intValue() == 1) {
			this.getLOGGER().fine("Abdulla");
			System.out.println("Infinite Thread");
			Thread.sleep(1000000L);
		}
	}

	@Override
	public void rollback() {
		System.out.println("RollBack");
		if (incrementExecuted) {
			this.count.count.decrementAndGet();
		}
		
	}

}
