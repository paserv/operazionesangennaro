package it.cloudpanel.servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class BulkUploaderServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

//		String delimiter = req.getParameter("delimiter");
//		String group = req.getParameter("group");
//		String entity = req.getParameter("entity");
//		String data = req.getParameter("data");
//
//		String[] headers = getHeaders(data, delimiter);
//		ArrayList<String[]> dataSet = getData(data, delimiter);
//		
//		//Key questionarioKey = KeyFactory.createKey("Domande", group);
//		
//		for(int j=0; j<dataSet.size();j++){
//			String[] currRow = dataSet.get(j);
//			Entity currEntity = new Entity(entity, currRow[0]);
//			for(int i =1; i<currRow.length;i++){
//				System.out.println(headers[i] + ": " + currRow[i]);
//				Object value = null;
//				boolean isString = true;
//				try {
//					value = Integer.parseInt(currRow[i]);
//					isString = false;
//					System.out.println("INTERO");
//				} catch (Exception e) {
//					
//				}
//				
//				if (currRow[i].equalsIgnoreCase("true") || currRow[i].equalsIgnoreCase("false")){
//					value = Boolean.parseBoolean(currRow[i]);
//					isString = false;
//					System.out.println("BOOLEANO");
//				}
//				
//								
//				if(isString) {
//					currEntity.setProperty(headers[i], currRow[i]);
//				} else {
//					currEntity.setProperty(headers[i], value);
//				}
//				
//				
//			}
//			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//			datastore.put(currEntity);
//		}
//		
////		resp.setContentType("text/plain");
////		resp.getWriter().println("Bulk Upload Done");
//		resp.sendRedirect("bulkdone.html");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String delimiter = req.getParameter("delimiter");
		String group = req.getParameter("group");
		String entity = req.getParameter("entity");
		String data = req.getParameter("data");

		String[] headers = getHeaders(data, delimiter);
		ArrayList<String[]> dataSet = getData(data, delimiter);
		
		//Key questionarioKey = KeyFactory.createKey("Domande", group);
		
		for(int j=0; j<dataSet.size();j++){
			String[] currRow = dataSet.get(j);
			Entity currEntity = new Entity(entity, currRow[0]);
			for(int i =1; i<currRow.length;i++){
				System.out.println(headers[i] + ": " + currRow[i]);
				Object value = null;
				boolean isString = true;
				try {
					value = Integer.parseInt(currRow[i]);
					isString = false;
					System.out.println("INTERO");
				} catch (Exception e) {
					
				}
				
				if (currRow[i].equalsIgnoreCase("true") || currRow[i].equalsIgnoreCase("false")){
					value = Boolean.parseBoolean(currRow[i]);
					isString = false;
					System.out.println("BOOLEANO");
				}
				
								
				if(isString) {
					currEntity.setProperty(headers[i], currRow[i]);
				} else {
					currEntity.setProperty(headers[i], value);
				}
				
				
			}
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(currEntity);
		}
		
		resp.sendRedirect("bulkdone.html");
	}

	

	
	private ArrayList<String[]> getData(String data, String delimiter) {
		String[] splittedCSV = data.split("\r\n");
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		for (int i=1; i<splittedCSV.length; i++){
			String[] splittedRow = splittedCSV[i].split(delimiter);
			result.add(splittedRow);
		}
		
		return result;
	}

	private String[] getHeaders(String data, String delimiter) {
		String[] splittedCSV = data.split("\r\n");
		String[] headers = splittedCSV[0].split(delimiter);
		return headers;
	}

}
