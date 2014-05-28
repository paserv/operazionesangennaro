package it.osg.data;

import com.google.common.collect.BoundType;
import com.google.common.collect.TreeMultiset;

public class RequestCounter {

	private TreeMultiset<Long> requests = TreeMultiset.create();
	
	public synchronized void addRequest(long timestamp) {
		requests.add(timestamp);
	}
	
	public synchronized long greaterThanNumber (long number) {
		requests = (TreeMultiset<Long>) requests.tailMultiset(number, BoundType.CLOSED);
		return requests.size();
	}
	
	public static void main(String[] args) {
		RequestCounter counter = new RequestCounter();
		counter.addRequest(450L);
		counter.addRequest(451L);
		counter.addRequest(452L);
		counter.addRequest(453L);
		counter.addRequest(454L);
		counter.addRequest(454L);
		counter.addRequest(454L);
		counter.addRequest(454L);
		counter.addRequest(4541L);
		
		System.out.println(counter.greaterThanNumber(453L));
		
		
	}
	
}
