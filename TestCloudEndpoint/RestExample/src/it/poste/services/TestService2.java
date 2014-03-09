package it.poste.services;

import it.poste.bean.TestBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/todo")
public class TestService2 {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public TestBean getXML() {
		TestBean todo = new TestBean();
		todo.setSummary("This is my first todo");
		todo.setDescription("This is my first todo");
		return todo;
	}

	// This can be used to test the integration with the browser
	@GET
	@Produces({ MediaType.TEXT_XML })
	public TestBean getHTML() {
		TestBean todo = new TestBean();
		todo.setSummary("This is my first todo");
		todo.setDescription("This is my first todo");
		return todo;
	}

}
