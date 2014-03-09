package osg.service.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import osg.service.endpoints.Mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailUtils {
	
	private static String MITTENTE = "donpablooooo@gmail.com";
	
	//public static void sendMail(String mail, String subject, String bodyMail, String attachFileName, String attachFile, String mailMittente, String mittente) {
	public static void sendMail(Mail mail) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(MITTENTE, mail.getMittente()));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getDestinatario()));
			msg.setSubject(mail.getOggetto());
//			msg.setText(mail.getText());

			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(mail.getText(), "text/plain");
			mp.addBodyPart(htmlPart);

//			MimeBodyPart attachment = new MimeBodyPart();
//			ByteArrayDataSource src = new ByteArrayDataSource(attachFile.getBytes(), "text/plain"); 
//			attachment.setFileName(attachFileName);
//			attachment.setDataHandler(new DataHandler (src));
//			mp.addBodyPart(attachment);

			msg.setContent(mp);
			msg.saveChanges();
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	

	}
	
	
} 
