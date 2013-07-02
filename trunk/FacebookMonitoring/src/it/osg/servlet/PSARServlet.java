package it.osg.servlet;

import it.osg.datasource.facebook.label.PSARData;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
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
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class PSARServlet extends HttpServlet {

	private static final long serialVersionUID = 8331147807173716595L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		String idSindaco =  req.getParameter("sindaco");
		String from = req.getParameter("from");
		String to = req.getParameter("to");

		String mailForDelivery = req.getParameter("mailTo");
		String strSubject = "Dati relativi alla pagina con ID " + idSindaco;

		//Iterate over array di sindaci
		PSARData service = new PSARData();
		Date startDate = DateUtils.getNowDate();
		ArrayList<Graph> result = service.getGraphData(new Object[]{idSindaco, from, to});
		Date endDate = DateUtils.getNowDate();
		
		String bodyMail = "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.getNow() + "\nTime Elapsed: " + DateUtils.secondiTraDueDate(startDate, endDate) +"\n\n"; 

		Entity currEntity = new Entity("queue");
		currEntity.setProperty("IDPagina", idSindaco);
		currEntity.setProperty("dataPrelievo", DateUtils.getNow());
		currEntity.setProperty("elapsedTime", DateUtils.secondiTraDueDate(startDate, endDate));
		try {
			currEntity.setProperty("from", DateUtils.parseDateAndTime(from));
			currEntity.setProperty("to", DateUtils.parseDateAndTime(to));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		

		bodyMail = bodyMail + "Risultati per la pagina con ID = " + idSindaco + "\n\n";

		for (int j = 0; j < result.size(); j++) {
			Graph currGraph = result.get(j);
			currEntity.setProperty(currGraph.getAxis(), currGraph.getOrdinate());

			bodyMail = bodyMail + currGraph.getAxis() + " = " + currGraph.getOrdinate() + "\n";
		}
		datastore.put(currEntity);


		//SEND MAIL
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("donpablooooo@gmail.com", "DONPABLOWATCH"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailForDelivery));
			msg.setSubject(strSubject);
			//msg.setText(bodyMail);

			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(bodyMail, "text/plain");
			mp.addBodyPart(htmlPart);

			MimeBodyPart attachment = new MimeBodyPart();
			ByteArrayDataSource src = new ByteArrayDataSource(bodyMail.getBytes(), "text/plain"); 
			attachment.setFileName(idSindaco + ".txt");
			attachment.setDataHandler(new DataHandler (src));
			mp.addBodyPart(attachment);

			msg.setContent(mp);
			msg.saveChanges();

			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}		

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}


}
