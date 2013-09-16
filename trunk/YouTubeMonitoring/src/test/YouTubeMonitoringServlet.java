package test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
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
			activityRequest.setChannelId("UC_x5XG1OV2P6uZZ5FSM9Ttw");
			activityRequest.setFields("items(id,snippet/title)");
			ActivityListResponse activities = activityRequest.execute();
			List<Activity> listOfChannels = activities.getItems();

			
			
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
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

}
