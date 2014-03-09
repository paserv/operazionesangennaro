package test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import it.osg.service.model.Edge;
import it.osg.service.model.Node;
import it.osg.service.model.GraphElement.ElementType;
import it.osg.utils.DateUtils;
import it.osg.utils.Utils;

public class Test {

	public static void main(String[] args) {

		Date tooo;
		try {
			tooo = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
			System.out.println(DateUtils.addMonthToDate(tooo, -1));
			if (DateUtils.diffInDay(tooo, DateUtils.getNowDate()) > 0) {
				System.out.println("OK");
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}


		Hashtable<String, Edge> edges = new Hashtable<String, Edge>();
		Edge edge1 = new Edge("nonpres", "pres", 1,ElementType.AUTHOR);
		Edge edge2 = new Edge("pres", "nonpres", 1,ElementType.AUTHOR);
		Edge edge3 = new Edge("nonpres", "nonpres", 1,ElementType.AUTHOR);
		Edge edge4 = new Edge("pres", "pres", 1,ElementType.AUTHOR);
		edges.put("nonpres_pres", edge1);
		edges.put("pres_nonpres", edge2);
		edges.put("nonpres_nonpres", edge3);
		edges.put("pres_pres", edge4);

		Hashtable<String, Node> nodes = new Hashtable<String, Node>();
		Node nod = new Node("pres", "ciao", 13, ElementType.AUTHOR);
		nodes.put("pres", nod);

		Enumeration<String> keys = edges.keys();
		while(keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			Edge currEdge = edges.get(currKey);
			if (nodes.containsKey(currEdge.source) && nodes.containsKey(currEdge.target))
				System.out.println("Edge" + currKey);
		}






		double numTotalePost = 41;
		double numGiorni = 4;

		double d = 5;
		long l = 6;

		System.out.println(d > l);

		String vir = "paolo \"ciao\" sono io";
		System.out.println(Utils.cleanString(vir));

		String numTask = "1";
		Long executedTask = 1L;

		if (executedTask != null) {
			if (Long.valueOf(numTask) == executedTask) {
				System.out.println("ciao");
			}
		}


		String from = "01-06-2013 00:00:00";
		String to = "02-06-2013 00:00:00";

		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(from);
			t = DateUtils.parseDateAndTime(to);
			System.out.println(DateUtils.giorniTraDueDate(f, t));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Queue queue = QueueFactory.getDefaultQueue();

		String from1 = from;
		while (true) {
			Date f1 = null;
			try {
				f1 = DateUtils.parseDateAndTime(from1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			from1 = DateUtils.formatDateAndTime(f1);
			Date t1 = DateUtils.addOneDay(f1);
			String to1 = DateUtils.formatDateAndTime(t1);

			if (DateUtils.compareDate(t, t1) >= 0) {
				System.out.println(from1.substring(0, 10) + " " + to1.substring(0, 10));
				from1 = DateUtils.formatDateAndTime(t1);

			} else {
				break;
			}
		}
		System.out.println(numTotalePost/DateUtils.giorniTraDueDate(f, t));


		try {
			System.out.println(Utils.MD5("dfhwdkljflkjwedfsd"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
