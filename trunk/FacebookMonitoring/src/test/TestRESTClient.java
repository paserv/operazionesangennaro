package test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class TestRESTClient {

	 
	public static void main(String[] args) {

		//TEST POST
		Form form = new Form();
		form.add("from", "15-05-2013 10:30:00");
		form.add("to", "15-05-2013 11:30:00");
		
		Client clientpost = Client.create();
		WebResource resource = clientpost.resource("http://localhost:8888/rest/resource/ballarotimeinterval").path("15-05-2013 10:30:00").path("15-05-2013 11:30:00");
		
		//ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, form);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		
		if (response.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
			System.out.println("Success! " + response.getStatus());
			System.out.println(response.getEntity(String.class));
		} else {
			System.out.println("ERROR! " + response.getStatus());    
			System.out.println(response.getEntity(String.class));
		}
	
	}
        
        
}
