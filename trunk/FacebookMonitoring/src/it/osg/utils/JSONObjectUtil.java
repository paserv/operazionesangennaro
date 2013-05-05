package it.osg.utils;

import org.json.simple.JSONObject;

public class JSONObjectUtil {

	public static String retrieveJsonPath (JSONObject json, String path){
		
		if (!path.contains("/"))  {
			if (json.get(path) !=null) {
				return json.get(path).toString();
			} else {
				System.out.println("No Path found");
				return "";
			}
			
		} else {
			String[] paths = path.split("/");
			JSONObject newJson = (JSONObject) json.get(paths[0]);
			String newPath = path.substring(path.indexOf("/") + 1);
			return retrieveJsonPath(newJson, newPath);
		}
	}
	
}
