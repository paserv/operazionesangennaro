package test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtubeAnalytics.YouTubeAnalytics;
import com.google.api.services.youtubeAnalytics.YouTubeAnalyticsRequestInitializer;
import com.google.api.services.youtubeAnalytics.model.ResultTable;

public class YouTubeMonitoringSimpleAPIAccessServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String API_KEY = "AIzaSyAtTQOEJNhjBSBMdh8FO8ih8ecnafNgjEo";

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport(); // NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of Youtube object to make general YouTube API requests. */
	private static YouTube youtube;

	/** Global instance of YoutubeAnalytics object to make analytic API requests. */
	private static YouTubeAnalytics analytics;




	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("TEST").setGoogleClientRequestInitializer(new YouTubeRequestInitializer(API_KEY)).build();

		try {

			YouTube.Activities.List activityRequest = youtube.activities().list("id,snippet,contentDetails");
			activityRequest.setChannelId("UCzovlWQBDtQCpYWZl1ly4jg");
			activityRequest.setFields("items(id,snippet,contentDetails)").setMaxResults(50L);
			ActivityListResponse activities = activityRequest.execute();
			List<Activity> activityList = activities.getItems();

			Iterator<Activity> iterAct = activityList.iterator();
			while (iterAct.hasNext()) {
				Activity curr = iterAct.next();
				out.println("ID: " + curr.getId() + "<br>");
				out.println("Kind: " + curr.getKind() + "<br>");
				out.println("Snippet: " + curr.getSnippet() + "<br>");
				if (curr.getSnippet() != null) {
					out.println("getPublishedAt: " + curr.getSnippet().getPublishedAt() + "<br>");
					out.println("getChannelId: " + curr.getSnippet().getChannelId() + "<br>");
					out.println("getTitle: " + curr.getSnippet().getTitle() + "<br>");
					out.println("getDescription: " + curr.getSnippet().getDescription() + "<br>");
				}
				if (curr.getContentDetails() != null) {
					out.println("getFavorite: " + curr.getContentDetails().getFavorite() + "<br>");
					out.println("getLike: " + curr.getContentDetails().getLike() + "<br>");
					out.println("getUpload: " + curr.getContentDetails().getUpload() + "<br>");
					if (curr.getContentDetails().getUpload() != null) {
						out.println("getVideoId: " + curr.getContentDetails().getUpload().getVideoId() + "<br>");
					}
				}
				
			}
			
//			YouTube.Videos.List videos = youtube.videos().list("id,snippet,contentDetails,fileDetails,player,processingDetails,recordingDetails,statistics,status,suggestions,topicDetails");
			YouTube.Videos.List videoRequest = youtube.videos().list("id,snippet,contentDetails,statistics,status,player");
			videoRequest.setId("Zs8xzED8jMI");
//			videoRequest.setId("VTEzNzk1MTA3NjAzNTE1NjI2MDI4");
			VideoListResponse videos = videoRequest.execute();
			List<Video> videoList = videos.getItems();
			Iterator<Video> iterVid = videoList.iterator();
			while (iterVid.hasNext()) {
				Video curr = iterVid.next();
				if (curr.getStatistics() != null) {
					out.println("getViewCount: " + curr.getStatistics().getViewCount() + "<br>");
					out.println("getLikeCount: " + curr.getStatistics().getLikeCount() + "<br>");
					out.println("getDislikeCount: " + curr.getStatistics().getDislikeCount() + "<br>");
					out.println("getFavoriteCount: " + curr.getStatistics().getFavoriteCount() + "<br>");
					out.println("getCommentCount: " + curr.getStatistics().getCommentCount() + "<br>");
				}
				
			}

			
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.println("END");

		//		analytics = new YouTubeAnalytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("TEST").setGoogleClientRequestInitializer(new YouTubeAnalyticsRequestInitializer(API_KEY)).build();
		//		YouTubeAnalytics.Reports rep = analytics.reports();
		//		
		//		ResultTable result = rep.query("channel==UC_x5XG1OV2P6uZZ5FSM9Ttw", "2012-01-01", "2012-01-14", "views,uniques").execute();
		//		
		//		out.println(result.toPrettyString());

		//		YouTubeAnalytics.Reports rep = analytics.reports();
		//		rep.query(arg0, arg1, arg2, arg3);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

}
