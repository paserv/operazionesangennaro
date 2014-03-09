package osg.service.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import osg.service.utils.Constants;

import javax.inject.Named;

import osg.service.utils.MailUtils;

@Api(
	    name = "mail",
	    version = "v1",
	    scopes = {Constants.EMAIL_SCOPE},
	    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
	    Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
	    audiences = {Constants.ANDROID_AUDIENCE}
	)
public class MailService {
	
	
	@ApiMethod(name = "send", httpMethod = "post")
	public Mail sendMail (@Named("destinatario") String destinatario, @Named("oggetto") String oggetto, @Named("text") String text, @Named("mittente") String mittente) {
		
		Mail mail = new Mail();
		mail.setDestinatario(destinatario);
		mail.setMittente(mittente);
		mail.setOggetto(oggetto);
		mail.setText(text);
		
		MailUtils.sendMail(mail);
		
		return mail;
	}
	
	@ApiMethod(name = "send.authed", path = "send/authed")
	public Mail authedSendMail (User user) {
	  Mail response = new Mail();
	  response.setMailMittente(user.getEmail());
	  return response;
	}

}