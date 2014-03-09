package test;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestRESTClient {

	public static void main(String[] args) {
		Document doc;
		try {
			doc = Jsoup.connect("http://08-monitorfacebookpages.appspot.com/rest/table/resource/" + "anagraficaSindaco" + "/" + "IDFacebook").userAgent("Mozilla").get();
			System.out.println(doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		Client client = Client.create();
//		WebResource webResource = client.resource("http://03-monitorfacebookpages.appspot.com/rest/table/resource/" + "anagraficaSindaco" + "/" + "IDFacebook");
//		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//		if (response.getStatus() != 201) {
//			System.out.println(response.getStatus());
//			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//		}
//		String output = response.getEntity(String.class);
//		System.out.println(output);
	}
	
}
