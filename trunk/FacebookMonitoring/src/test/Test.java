package test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import it.osg.utils.DateUtils;
import it.osg.utils.Utils;

public class Test {

	public static void main(String[] args) {
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
