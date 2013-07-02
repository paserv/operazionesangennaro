package it.osg.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailUtils {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String PORT="587";
	private static final String SMTP_AUTH_USER = "donpablooooo@gmail.com";
	private static final String SMTP_AUTH_PWD  = "gmaPaolos80";

	public static void main(String[] args) {
		
		System.getProperties().put("http.proxyHost", "localhost");
		System.getProperties().put("http.proxyPort", "5865");
//		System.getProperties().put("http.proxyUser", "someUserName");
//		System.getProperties().put("http.proxyPassword", "somePassword");
		
		MailUtils mu = new MailUtils();
		Authenticator auth = mu.new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(loadGmailProperties(),auth);
		mailSession.setDebug(true);
		try {
			Transport transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);
			message.setContent("Prova invio mail \n Non rispondere prego", "text/plain");
			message.setSubject("Invio di prova");
			message.setFrom(new InternetAddress("noreply@test.it"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress("paserv@gmail.com"));
			transport.connect();
			System.out.println("Invio....");
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			System.out.println("Inviato....");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}


	}

	public static void sendMail (String dest, String mitt, String oggetto, String testoEmail) throws MessagingException {

	}

	private static Properties loadGmailProperties(){
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.starttls.enable", "true");
		return props;
	}


	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
} 
