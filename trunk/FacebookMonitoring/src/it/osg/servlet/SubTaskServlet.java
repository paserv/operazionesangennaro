package it.osg.servlet;

import it.osg.utils.DateUtils;
import it.osg.utils.ShardedCounter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SubTaskServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String pageId = "";
	protected String idTransaction = "";
	protected Date from;
	protected Date to;

	protected abstract void runSubTask();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		idTransaction = req.getParameter("idTransaction");
		pageId = req.getParameter("pageId");
		String f = req.getParameter("from");
		String t = req.getParameter("to");

		//GET DATE
		from = null;
		to = null;
		try {
			from = DateUtils.parseDateAndTime(f);
			to = DateUtils.parseDateAndTime(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		runSubTask();
		
		//SAVE DATA TO DATASTORE AND INCREMENT 1 TASK
		ShardedCounter counter = new ShardedCounter();
		counter.increment(idTransaction);
		
			
	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



}
