package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.*;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequestInitializer;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;






@SuppressWarnings("serial")
public class TestGPServlet extends HttpServlet {
	
	private static final String API_KEY = "AIzaSyB8YOf4t4cwdahoOgb8QPyLF4asGXtNlvo";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		HttpTransport httpTransport = new UrlFetchTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		//Plus plus = new Plus(httpTransport, jsonFactory, null); 

		Plus plus = new Plus.Builder(httpTransport, jsonFactory, null).setApplicationName("TEST").setGoogleClientRequestInitializer(new PlusRequestInitializer(API_KEY)).build();

		ActivityFeed myActivityFeed = plus.activities().search("matteorenzi").execute();
		List<Activity> myActivities = myActivityFeed.getItems();

		for (Activity a : myActivities) {
			System.out.println(a.getTitle());
			out.println(a.getTitle());
		}
	}
}
