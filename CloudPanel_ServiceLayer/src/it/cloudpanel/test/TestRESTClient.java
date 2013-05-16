package it.cloudpanel.test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class TestRESTClient {

	 
	public static void main(String[] args) {

		//TEST POST
		Form form = new Form();
		form.add("from", "15-05-2013 10:30:00");
		form.add("to", "15-05-2013 11:30:00");
		
		Client clientpost = Client.create();
		WebResource resource = clientpost.resource("http://localhost:8888/rest/resource/ballarotimeinterval");
		
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, form);

		if (response.getClientResponseStatus().getFamily() == Family.SUCCESSFUL) {
			System.out.println("Success! " + response.getStatus());
			System.out.println(response.getEntity(String.class));
		} else {
			System.out.println("ERROR! " + response.getStatus());    
			System.out.println(response.getEntity(String.class));
		}
		
		        
//		ClientConfig config = new DefaultClientConfig();
//		Client clientget = Client.create(config);
//
//		Client clientget = createClient();
//		
//		WebResource service = clientget.resource("http://localhost:8080/");
//		// Get XML
//		System.out.println(service.path("rest").path("resource").path("domande").accept(MediaType.APPLICATION_XML).get(String.class));
		
	}
        
        private static Client createClient() {
        	 
            final String proxyHost = "proxy.gss.rete.poste";
            final String proxyPort = "8080";
             
            final DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
            if (proxyHost!=null && proxyPort!=null) {
                 
                config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://" + proxyHost + ":" + proxyPort);
                 
                final String proxyUser = "rete/servill7";
                final String proxyPassword = "Paolos91";
                if (proxyUser!=null && proxyPassword!=null) {
                    config.getState().setProxyCredentials(null, proxyHost, Integer.parseInt(proxyPort), proxyUser, proxyPassword);
                } 
            }
             
            return ApacheHttpClient.create(config);
        }


}
