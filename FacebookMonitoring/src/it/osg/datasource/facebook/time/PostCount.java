package it.osg.datasource.facebook.time;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.auth.AccessToken;
import it.osg.datasource.SourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


/* 
 * Preleva i dati direttamente da Facebook ed in base ai dati che si vuole tirar fuori restituisce un ArrayList<GraphData>
 */

public class PostCount extends SourceGenerator {


	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {
		ArrayList<Graph> result = new ArrayList<Graph>();

		Date f = null;
		Date t = null;
		try {
			if (objects[0] != null && objects[1] != null){
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}

			Facebook facebook = new FacebookFactory().getInstance();
			facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
			//facebook.setOAuthPermissions(commaSeparetedPermissions);
			facebook.setOAuthAccessToken(new AccessToken("156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk", null));
			
			ArrayList<Post> posts = FacebookUtils.getAllPosts(facebook, (String)objects[0], f, t);
			
			Iterator<Post> iter = posts.iterator();
			while (iter.hasNext()){
				Post curr = iter.next();
				Graph gd = new Graph(DateUtils.formatDateAndTime(curr.getCreatedTime()), 1L);
				result.add(gd);
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}


}
