package it.cloudpanel.servlet;

import it.cloudpanel.service.converter.DomandaConverter;
import it.cloudpanel.service.model.Domanda;
import it.cloudpanel.service.rest.impl.DomandeResource;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		
		DomandeResource res = new DomandeResource();
		
		ArrayList<Domanda> result = res.getDomande(); 
		
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < result.size(); i++) {
			Domanda curr = result.get(i);
			buf.append(curr.toString());
		}
		
		//res.putDomanda("CIAO");
		
		resp.setContentType("text/plain");
		resp.getWriter().println(buf.toString());
	}

}
