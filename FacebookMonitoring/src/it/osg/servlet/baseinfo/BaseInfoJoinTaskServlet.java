package it.osg.servlet.baseinfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import it.osg.servlet.JoinTaskServlet;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.Utils;

public class BaseInfoJoinTaskServlet extends JoinTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected long getTimeout() {
		return (Long) DatastoreUtils.getValue("conf", "property", "jointimeout", "value");
	}


	@Override
	protected long getDelay() {
		return (Long) DatastoreUtils.getValue("conf", "property", "joindelay", "value");
	}


	@Override
	protected String getSubjectMail() {
		return "Dati relativi alla pagina con ID " + pageId;
	}


	@Override
	protected String getBodyMail() {
		return "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
	}


	@Override
	protected String getAttachFileName() {
		return pageId + ".csv";
	}


	@Override
	protected String getAttachFile() {
		String result = "ID,Nome,TotalFan,TalkingAbout,FirstPost\n";
		
		ArrayList<String> pages = new ArrayList<String>();
		if (!Utils.isDouble(pageId)) {
			pages = DatastoreUtils.getKeyNamesFromTable(pageId);
		} else {
			pages.add(pageId);
		}
		Iterator<String> iter = pages.iterator();
		while (iter.hasNext()) {
			String currPage = iter.next();
			Hashtable<String, Object> bi = FacebookUtils.getBaseInfo(currPage);
			String startDate = DatastoreUtils.getValue("firstpostofpages", "key", currPage, "startdate").toString();
			result = result + currPage + "," + bi.get("name") + "," + bi.get("likes") + "," + bi.get("talking_about_count") + "," + startDate + "\n";
		}
		return result;		
	}


	@Override
	protected String getJoinTaskName() {
		return "baseinfojointask";
	}
	
	

}
