package it.osg.servlet.baseinfo;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import it.osg.servlet.SubTaskServlet;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import java.text.ParseException;
import java.util.Date;
import com.google.appengine.api.datastore.Entity;

public class BaseInfoSubTaskServlet extends SubTaskServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void runSubTask() {
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		t = DateUtils.addMonthToDate(f, 1);
		try {
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				ResponseList<Post> facResults = FacebookUtils.getFB().getFeed(pageId, new Reading().since(f).until(t).fields("created_time").limit(1));
				if (facResults != null && facResults.size() != 0) {
					Post currPost = facResults.get(0);
					Entity ent = new Entity("firstpostofpages", pageId);
					ent.setUnindexedProperty("startdate", currPost.getCreatedTime());
					DatastoreUtils.saveEntity(ent);
					break;
				} else {
					f = t;
					t = DateUtils.addMonthToDate(f, 1);
				}
			}

		} catch (FacebookException e) {
			e.printStackTrace();
		}

	}



}
