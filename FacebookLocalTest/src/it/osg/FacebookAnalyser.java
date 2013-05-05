package it.osg;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FacebookAnalyser {

	public static ArrayList<Hashtable<String, String>> analyse(String jsonString) {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		Object objJson = JSONValue.parse(jsonString);
		JSONObject json =(JSONObject) objJson;
		JSONArray dataArray = (JSONArray) json.get("data");
		String time = String.valueOf(System.currentTimeMillis());
		if (dataArray != null){
			for (int i = 0; i < dataArray.size(); i++) {
				Hashtable<String, String> currRow = new Hashtable<String, String>();
				currRow.put("timestamp", time);
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
					int post_likes = 0;
					if (JSONObjectUtil.retrieveJsonPath(currJson, "likes/count") != null) {
						post_likes = Integer.parseInt(JSONObjectUtil.retrieveJsonPath(currJson, "likes/count"));
					}
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
