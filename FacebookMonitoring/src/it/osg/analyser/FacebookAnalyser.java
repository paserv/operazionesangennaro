package it.osg.analyser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import it.osg.utils.JSONObjectUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FacebookAnalyser {
	
	public static ArrayList<Hashtable<String, Object>> likeTalkAnalysis(String jsonString) {
		ArrayList<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;
		
		Hashtable<String, Object> currRow = new Hashtable<String, Object>();
		
		String likeCount = JSONObjectUtil.retrieveJsonPath(json, "likes");
		String talkingAboutCount = JSONObjectUtil.retrieveJsonPath(json, "talking_about_count");
		
		System.out.println(likeCount + " " + talkingAboutCount);
		
		currRow.put("like_count", Integer.parseInt(likeCount));
		currRow.put("talking_about_count", Integer.parseInt(talkingAboutCount));
		
		Calendar cal= Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.HOUR_OF_DAY, TimeZone.getTimeZone("CEST").getRawOffset());
		SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date cestDate= cal.getTime();
		
		currRow.put("timestamp", cal.getTimeInMillis());
        currRow.put("date", cestDate);
        
        //System.out.println(cal.getTimeInMillis() + " " + sdf.format(cestDate));
		
        result.add(currRow);
		
		return result;
	}

	public static ArrayList<Hashtable<String, String>> analyse(String jsonString) {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;
		JSONArray dataArray = (JSONArray) json.get("data");
		if (dataArray != null){
			for (int i = 0; i < dataArray.size(); i++) {
				Hashtable<String, String> currRow = new Hashtable<String, String>();
				currRow.put("timestamp", String.valueOf(System.currentTimeMillis()));
				JSONObject currJson =(JSONObject) dataArray.get(i);
				String postId = JSONObjectUtil.retrieveJsonPath(currJson, "id");
				currRow.put("postId", postId);
				String object_id = JSONObjectUtil.retrieveJsonPath(currJson, "object_id");
				currRow.put("postId", object_id);
				String created_time = JSONObjectUtil.retrieveJsonPath(currJson, "created_time");
				currRow.put("created_time", created_time);
				String updated_time = JSONObjectUtil.retrieveJsonPath(currJson, "updated_time");
				currRow.put("updated_time", updated_time);
				JSONObject currComments = (JSONObject) currJson.get("comments");
				if (currComments != null){
					JSONArray currCommentsArray = (JSONArray) currComments.get("data");
					String comment_number = String.valueOf(currCommentsArray.size());
					currRow.put("comment_number", comment_number);
					int post_likes = Integer.parseInt(JSONObjectUtil.retrieveJsonPath(currJson, "likes/count"));
					currRow.put("post_likes", String.valueOf(post_likes));
					int comments_like = 0;
					for (int j = 0; j < currCommentsArray.size(); j++) {
						JSONObject currJsonComment =(JSONObject) currCommentsArray.get(j);
						String like_count = JSONObjectUtil.retrieveJsonPath(currJsonComment, "like_count");;
						int currCommLike = Integer.parseInt(like_count);
						comments_like = comments_like + currCommLike;
					}
					String lik = String.valueOf(comments_like);
					currRow.put("comments_like", lik);
				}
				result.add(currRow);
			}
			
		}

		
		return result;
	}

}
