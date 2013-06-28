package test;

import java.text.ParseException;
import java.util.Date;

import it.osg.utils.DateUtils;

public class Test {

	public static void main(String[] args) {
		double numTotalePost = 41;
		double numGiorni = 4;


		String from = "01-06-2013 00:00:00";
		String to = "19-06-2013 10:07:06";
		
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(from);
			t = DateUtils.parseDateAndTime(to);
			System.out.println(DateUtils.giorniTraDueDate(f, t));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		

		System.out.println(numTotalePost/DateUtils.giorniTraDueDate(f, t));

	}

}
