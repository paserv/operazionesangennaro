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
import com.google.api.services.youtubeAnalytics.YouTubeAnalytics;
import com.google.api.services.youtubeAnalytics.YouTubeAnalyticsRequestInitializer;
import com.google.api.services.youtubeAnalytics.model.ResultTable;

public class YouTubeMonitoringServlet extends HttpServlet {

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
			activityRequest.setFields("items(id,snippet/title)").setMaxResults(50L);
			ActivityListResponse activities = activityRequest.execute();
			List<Activity> listOfChannels = activities.getItems();

			Iterator<Activity> iter = listOfChannels.iterator();
			while (iter.hasNext()) {
				Activity curr = iter.next();
				out.println("ID: " + curr.getId() + "<br>");
				out.println("Kind: " + curr.getKind() + "<br>");
				out.println("Snippet: " + curr.getSnippet() + "<br>");
			}
			
//			Channel defaultChannel = listOfChannels.get(0);
//			String channelId = defaultChannel.getId();
//
//			PrintStream writer = System.out;
//			if (channelId == null) {
//				writer.println("No channel found.");
//			} else {
//				writer.println("Default Channel: " + defaultChannel.getSnippet().getTitle() + " ( " + channelId + " )\n");
//			}
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
