package it.osg.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtubeAnalytics.YouTubeAnalytics;

public class YouTubeUtils {

	private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport(); // NetHttpTransport();

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static YouTube youtube = null;

	private static YouTubeAnalytics analytics = null;

	public static YouTube getYT() {
		if (youtube != null) {
			return youtube;
		}
		YouTube yt = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("TEST").setGoogleClientRequestInitializer(new YouTubeRequestInitializer(Constants.YOUTUBE_API_KEY)).build();
		youtube = yt;
		return youtube;
	}

	public static List<Activity> getActivities(String userId, Date from, Date to) {
		DateTime fr = new DateTime(from);
		DateTime t = new DateTime(to);
		try {
			YouTube.Activities.List activityRequest = getYT().activities().list("id,snippet,contentDetails");
			activityRequest.setChannelId(userId);
			activityRequest.setFields("items(id,snippet,contentDetails)");
//			activityRequest.setMaxResults(50L);
			activityRequest.setPublishedAfter(fr);
			activityRequest.setPublishedBefore(t);
			ActivityListResponse activities = activityRequest.execute();
			List<Activity> activityList = activities.getItems();
			return activityList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Hashtable<String, BigInteger> getAllUserInteraction(List<Activity> activ) {
		Hashtable<String, BigInteger> result = new Hashtable<String, BigInteger>();
		result.put("viewcount", BigInteger.valueOf(0));
		result.put("likecount", BigInteger.valueOf(0));
		result.put("dislikecount", BigInteger.valueOf(0));
		result.put("favouritecount", BigInteger.valueOf(0));
		result.put("commentcount", BigInteger.valueOf(0));
		Iterator<Activity> iterAct = activ.iterator();
		while (iterAct.hasNext()) {
			Hashtable<String, BigInteger> activityUserInteraction = getUserInteraction(iterAct.next());
			result.put("viewcount", result.get("viewcount").add(activityUserInteraction.get("viewcount")));
			result.put("likecount", result.get("likecount").add(activityUserInteraction.get("likecount")));
			result.put("dislikecount", result.get("dislikecount").add(activityUserInteraction.get("dislikecount")));
			result.put("favouritecount", result.get("favouritecount").add(activityUserInteraction.get("favouritecount")));
			result.put("commentcount", result.get("commentcount").add(activityUserInteraction.get("commentcount")));
		}
		return result;
	}

	public static Hashtable<String, BigInteger> getUserInteraction(Activity act) {
		Hashtable<String, BigInteger> result = new Hashtable<String, BigInteger>();
		result.put("viewcount", BigInteger.valueOf(0));
		result.put("likecount", BigInteger.valueOf(0));
		result.put("dislikecount", BigInteger.valueOf(0));
		result.put("favouritecount", BigInteger.valueOf(0));
		result.put("commentcount", BigInteger.valueOf(0));
		if (act.getContentDetails() != null && act.getContentDetails().getUpload() != null) {
			String videoId = act.getContentDetails().getUpload().getVideoId();
			try {
				YouTube.Videos.List videoRequest = getYT().videos().list("id,snippet,contentDetails,statistics,status,player");
				videoRequest.setId(videoId);
				VideoListResponse videos = videoRequest.execute();
				List<Video> videoList = videos.getItems();
				if (videoList.get(0) != null) {
					Video myVid = videoList.get(0);
					if (myVid.getStatistics() != null) {
						result.put("viewcount", myVid.getStatistics().getViewCount());
						result.put("likecount", myVid.getStatistics().getLikeCount());
						result.put("dislikecount", myVid.getStatistics().getDislikeCount());
						result.put("favouritecount", myVid.getStatistics().getFavoriteCount());
						result.put("commentcount", myVid.getStatistics().getCommentCount());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Hashtable<String, String> getBaseInfo(String userId) {
		Hashtable<String, String> result = new Hashtable<String, String>();
		String url = Constants.YOUTUBE_CHANNEL_ROOT_URL + userId + "/about";
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
			Elements aboutInfo = doc.select("span.about-stat-value");
			if (aboutInfo != null && aboutInfo.get(0) != null) {
				result.put("subscribers", aboutInfo.get(0).text());
			}
			if (aboutInfo != null && aboutInfo.get(1) != null) {
				result.put("views", aboutInfo.get(1).text());
			}
			Element joinedDate = doc.select("span.value").first();
			if (joinedDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
				try {
					result.put("joineddate", sdf2.format(sdf.parse(joinedDate.text())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getUserId(String userName) {
		String url = Constants.YOUTUBE_USER_ROOT_URL + userName + "/about";
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
			Element channelId = doc.getElementsByAttributeValue("itemprop", "channelId").first();
			return channelId.attr("content");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "channelId non trovato";
	}

}
