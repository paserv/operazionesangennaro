package it.osg.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Console {
	private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
	private static boolean running = false;
	
	private static String fileName;
	private static String from;
	private static String to;

	public static void main(String[] args) {

		System.out.println("Actions: run - exit - help");
		System.out.print("> ");
		String action = getParameter();
		if (action.equalsIgnoreCase("run")) {
			running = true;
			System.out.println("Set input file from list");
			ArrayList<String> filesName = readInputFile("resources");
			getFileName(filesName);

			System.out.println("Set FROM date (dd-mm-yyyy HH:mm:ss)");
			getFromDate();
			
			System.out.println("Set TO date (dd-mm-yyyy HH:mm:ss)");
			getToDate();
			
			
			System.out.println(fileName);
			System.out.println(from);
			System.out.println(to);
			
			running = false;
			main(null);
		} else {
			main(null);
		}


	}

	private static void getToDate() {
		System.out.print("> ");
		String param = getParameter();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setLenient(false);
		
		try {
			Date toDate = sdf.parse(param);
			Date fromDate = sdf.parse(from);
			
			Calendar toCal = new GregorianCalendar();
			Calendar fromCal = new GregorianCalendar();
			toCal.setTime(toDate);
			fromCal.setTime(fromDate);
			
			if (fromCal.before(toCal)) {
				to = param;
			} else {
				System.out.println("TO date is before FROM date, retry!");
				System.out.print("> ");
				getToDate();
			}
			
			
		} catch (ParseException e) {
			System.out.println("Wrong date, retry!");
			System.out.print("> ");
			getToDate();
		}
		
	}

	private static void getFromDate() {
		System.out.print("> ");
		String param = getParameter();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setLenient(false);

		try {
			//if not valid, it will throw ParseException
			Date date = sdf.parse(param);
			from = param;

		} catch (ParseException e) {
			System.out.println("Wrong date, retry!");
			System.out.print("> ");
			getFromDate();
		}

	}

	private static void getFileName(ArrayList<String> list) {
		System.out.print("> ");
		String param = getParameter();
		for (String string : list) {
			if (string.equals(param)) {
				fileName = param;
				return;
			} else {
				System.out.println("File name not present, retry!");
				getFileName(list);
			}
		}
	}

	private static String getParameter() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try {
			String s = bufferRead.readLine();
			if (s.equalsIgnoreCase("exit")) {
				System.exit(0);
			} else if (s.equalsIgnoreCase("help") && running == false) {
				System.out.println("HELP");
				System.out.print("> ");
			}
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			return getParameter();
		}
	}


	private static ArrayList<String> readInputFile(String folder) {
		ArrayList<String> output = new ArrayList<String>();
		File folderFile = new File(folder);
		if (folderFile.isDirectory()) {
			File[] listOfFiles = folderFile.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					output.add(file.getName());
					System.out.println("\t" + file.getName());
				}
			}
		}
		return output;
	}

}
