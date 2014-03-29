package osg.service.endpoints;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FBStreamEndpoint extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		String hub_mode = req.getParameter("hub.mode");
		String hub_challenge = req.getParameter("hub.challenge");
		String hub_verify_token = req.getParameter("hub.verify_token");
		if (hub_verify_token.equalsIgnoreCase("stabbene") && hub_mode.equalsIgnoreCase("subscribe")) {
			resp.getWriter().println(hub_challenge);
		}
		
	}
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	

}