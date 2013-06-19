package it.osg.servlet;

import it.osg.datasource.facebook.label.SindacoData;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SindaciServlet extends HttpServlet {

	private static final long serialVersionUID = 8331147807173716595L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.println("Sindaco,MediaPost" + "</br>");
		
		//TODO get array sindaci
		ArrayList<String> sindaci = new ArrayList<String>();
		sindaci.add("Ballaro.Rai");
		
		//TODO get from e to
		String from = "01-06-2013 00:00:00";
		String to = "19-06-2013 10:07:06";
		
		//Iterate over array di sindaci
		for (int i = 0; i < sindaci.size(); i++) {
			SindacoData service = new SindacoData();
			ArrayList<Graph> result = service.getGraphData(new Object[]{sindaci.get(i), from, to});
			
			for (int j = 0; j < result.size(); j++) {
				Graph currGraph = result.get(j);
				out.println(sindaci.get(i) + "," + currGraph.getOrdinate() + "</br>");
			}
		}
		
		out.println("FINE");
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	
}
