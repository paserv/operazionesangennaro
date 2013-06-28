package it.osg.servlet;

import it.osg.datasource.facebook.label.PSARData;
import it.osg.service.model.Graph;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PSARServlet extends HttpServlet {

	private static final long serialVersionUID = 8331147807173716595L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.println("Sindaco,MediaPost" + "</br>");
		
		//TODO get array sindaci
		ArrayList<String> sindaci = new ArrayList<String>();
		//sindaci.add("56208847866"); //Gianni Alemanno
		sindaci.add("Ballaro.Rai"); //Gianni Alemanno
		
		//TODO get from e to
		String from = "01-06-2013 00:00:00";
		String to = "19-06-2013 10:07:06";
		
		//Iterate over array di sindaci
		for (int i = 0; i < sindaci.size(); i++) {
			PSARData service = new PSARData();
			ArrayList<Graph> result = service.getGraphData(new Object[]{sindaci.get(i), from, to});
			
			out.println(sindaci.get(i) + ",");
			for (int j = 0; j < result.size(); j++) {
				Graph currGraph = result.get(j);
				out.println(currGraph.getOrdinate() + ",");
			}
			out.println("</br>");
		}
		
		
		
		out.println("FINE");
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	
}
