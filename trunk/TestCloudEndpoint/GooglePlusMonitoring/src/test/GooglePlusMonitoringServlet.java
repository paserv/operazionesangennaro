package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

@SuppressWarnings("serial")
public class GooglePlusMonitoringServlet extends HttpServlet {
private static final String API_KEY = "AIzaSyB8YOf4t4cwdahoOgb8QPyLF4asGXtNlvo";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		ArrayList<String> ids = new ArrayList<String>();
		
		ids.add("111773660654455028752");
		ids.add("112201445140396915197");
		ids.add("107314507209079315166");
		ids.add("118153610117296026954");
		ids.add("112347002381912902209");
		ids.add("105387064317605853934");
		ids.add("105371282129290470875");
		ids.add("106724921500143225750");
		ids.add("112572168167100962884");
		ids.add("101945531327240477930");
		ids.add("113802278678161542105");
		ids.add("113726706582449014146");
		ids.add("103768171157932364023");
		ids.add("100110855571813757823");
		ids.add("108085461143026524937");
		ids.add("102772238226203339456");
		ids.add("101350017076266560399");
		ids.add("113998323170598320331");
		ids.add("101701993100568630623");
		ids.add("117761061796096195453");
		ids.add("106329876687222281908");
		ids.add("109284644458858129577");
		ids.add("103711728979005262206");
		ids.add("109520062735328180020");
		ids.add("106666939653064306989");
		ids.add("112805691856048807204");
		ids.add("106590037983789925298");
		ids.add("106684908758915438055");
		ids.add("112410765867033790347");
		ids.add("103127315696295318351");
		ids.add("112029310994250488753");
		ids.add("102040264075042366268");
		ids.add("101702210702016364302");
		
		
		
		HttpTransport httpTransport = new UrlFetchTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		//Plus plus = new Plus(httpTransport, jsonFactory, null); 

		Plus plus = new Plus.Builder(httpTransport, jsonFactory, null).setApplicationName("TEST").setGoogleClientRequestInitializer(new PlusRequestInitializer(API_KEY)).build();
//
//		ActivityFeed myActivityFeed = plus.activities().search("106590037983789925298").execute();
//		List<Activity> myActivities = myActivityFeed.getItems();
//
//		for (Activity a : myActivities) {
//			out.println(a.getTitle());
//			out.println(a.getTitle() + "<br>");
//		}
		
		for (int i = 0; i < ids.size(); i++) {
			String currId = ids.get(i);
			
			Person mePerson = plus.people().get(currId).execute();

			out.println("ID:\t" + mePerson.getId() + ",");
			out.println("Display Name:\t" + mePerson.getDisplayName() + ",");
			
//			out.println("About Me:\t" + mePerson.getAboutMe());
//			out.println("Birthday:\t" + mePerson.getBirthday());
//			out.println("BraggingRights:\t" + mePerson.getBraggingRights());
//			out.println("CurrentLocation:\t" + mePerson.getCurrentLocation());
//			out.println("Etag:\t" + mePerson.getEtag());	
			out.println("Gender:\t" + mePerson.getGender() + ",");
			out.println("Kind:\t" + mePerson.getKind() + ",");
//			out.println("Language:\t" + mePerson.getLanguage());
//			out.println("Nickname:\t" + mePerson.getNickname());
//			out.println("ObjectType:\t" + mePerson.getObjectType());
//			out.println("RelationshipStatus:\t" + mePerson.getRelationshipStatus());
//			out.println("Tagline:\t" + mePerson.getTagline());
//			out.println("Profile URL:\t" + mePerson.getUrl());
			out.println("CircledByCount:\t" + mePerson.getCircledByCount() + ",");
			out.println("PlusOneCount:\t" + mePerson.getPlusOneCount() + ",");
			//out.println("AgeRange:\t" + mePerson.getAgeRange().getMin() + "-" + mePerson.getAgeRange().getMax());
//			out.println("IsPlusUser:\t" + mePerson.getIsPlusUser());
//			out.println("Name:\t" + mePerson.getName().getFormatted());
			//out.println("Organizations size:\t" + mePerson.getOrganizations().size());
			//out.println("PlacesLived size:\t" + mePerson.getPlacesLived().size());
			out.println("Verified:\t" + mePerson.getVerified() + "<br>");
//			out.println("Image URL:\t" + mePerson.getImage().getUrl());
			
		}
		
		
		
		
	}
}
