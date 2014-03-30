package osg.service.endpoints;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import osg.service.utils.Constants;

import javax.inject.Named;

@Api(
	    name = "datastore",
	    version = "v1",
	    scopes = {Constants.EMAIL_SCOPE},
	    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
	    Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
	    audiences = {Constants.ANDROID_AUDIENCE}
	)
public class DatastoreServices {
	
	@ApiMethod(name = "registerEntity", httpMethod = "post")
	public void registerEntity (@Named("id") String id, @Named("name") String name, @Named("session") String session) {
		DatastoreService DS = DatastoreServiceFactory.getDatastoreService();
		Entity ent = new Entity(session, id);
		ent.setProperty("nome", name);
		DS.put(ent);
		
	}
	
	@ApiMethod(name = "saveProperty", httpMethod = "post")
	public void saveProperty (@Named("id") String id, @Named("session") String session, @Named("propname") String propName, @Named("propvalue") long propValue) {
		DatastoreService DS = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, KeyFactory.createKey(session, id));
		q = new Query(session).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			ent.setProperty(propName, propValue);
			DS.put(ent);
			return;
		}
	}
	
	@ApiMethod(name = "getEntities", httpMethod = "get")
	public List<PSAREntity> getEntity (@Named("session") String table) {
		
		List<PSAREntity> result = new ArrayList<PSAREntity>();
		
		DatastoreService DS = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		q = new Query(table);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			
			String id = (String) ent.getKey().getName();
			String name = (String) ent.getProperty("nome");
			
			long numCommToFanPost = 0;
			if (ent.getProperty("numCommToFanPost") != null) {
				numCommToFanPost = (long) ent.getProperty("numCommToFanPost");
			}
			
			long numCommToOwnPost = 0;
			if (ent.getProperty("numCommToOwnPost") != null) {
				numCommToOwnPost = (long) ent.getProperty("numCommToOwnPost");
			}
			
			long numLikeToOwnPost = 0;
			if (ent.getProperty("numLikeToOwnPost") != null) {
				numLikeToOwnPost = (long) ent.getProperty("numLikeToOwnPost");
			}
			
			long numOtherPost = 0;
			if (ent.getProperty("numOtherPost") != null) {
				numOtherPost = (long) ent.getProperty("numOtherPost");
			}
			
			long numOwnPost = 0;
			if (ent.getProperty("numOwnPost") != null) {
				numOwnPost = (long) ent.getProperty("numOwnPost");
			}
			
			long numShareToOwnPost = 0;
			if (ent.getProperty("numShareToOwnPost") != null) {
				numShareToOwnPost = (long) ent.getProperty("numShareToOwnPost");
			}
			
			long numOwnComm = 0;
			if (ent.getProperty("numOwnComm") != null) {
				numOwnComm = (long) ent.getProperty("numOwnComm");
			}
			
			result.add(new PSAREntity(id, name, numCommToFanPost, numCommToOwnPost, numLikeToOwnPost, numOtherPost, numOwnPost, numShareToOwnPost, numOwnComm));
		}
		
		return result;
	}
	
//	@ApiMethod(name = "send", httpMethod = "post")
//	public Mail sendMail (@Named("destinatario") String destinatario, @Named("oggetto") String oggetto, @Named("text") String text, @Named("mittente") String mittente) {
//		
//		Mail mail = new Mail();
//		mail.setDestinatario(destinatario);
//		mail.setMittente(mittente);
//		mail.setOggetto(oggetto);
//		mail.setText(text);
//		
//		MailUtils.sendMail(mail);
//		
//		return mail;
//	}
//	
//	@ApiMethod(name = "send.authed", path = "send/authed")
//	public Mail authedSendMail (User user) {
//	  Mail response = new Mail();
//	  response.setMailMittente(user.getEmail());
//	  return response;
//	}

}