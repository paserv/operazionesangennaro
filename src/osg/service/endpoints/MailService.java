package osg.service.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import javax.inject.Named;

import osg.service.utils.MailUtils;

@Api(name = "mail", version = "v1")
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

}