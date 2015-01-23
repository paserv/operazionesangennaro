package it.multithread.spider.bean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LinksMap {

	private HashMap<String, String> postLinks = new HashMap<String, String>();
	private HashMap<String, String> exploredLinks = new HashMap<String, String>();
	

	public synchronized void addExploredLink(String link) {
		exploredLinks.put(link, link);
	}
	
	public synchronized void addPostLink(String link) {
		postLinks.put(link, link);
	}
	
	public synchronized boolean isAlreadyExplored(String link) {
		return exploredLinks.containsKey(link);
	}
	
	public synchronized boolean isPostLink(String link) {
		return postLinks.containsKey(link);
	}
	
	public HashMap<String, String> getPostLinks() {
		return postLinks;
	}
	
	public void printExploredLinks(long time) {
		try {
			FileWriter fstream = new FileWriter("exploredLinks.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("SIZE: " + exploredLinks.size() + " ELAPSED TIME: " + time);
			out.newLine();
			for (String currLink : exploredLinks.keySet()) {
				out.write(currLink);
				out.newLine();
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void printPostLinks(long time) {
		try {
			FileWriter fstream = new FileWriter("postLinks.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("SIZE: " + postLinks.size() + " ELAPSED TIME: " + time);
			out.newLine();
			for (String currLink : postLinks.keySet()) {
				out.write(currLink);
				out.newLine();
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
