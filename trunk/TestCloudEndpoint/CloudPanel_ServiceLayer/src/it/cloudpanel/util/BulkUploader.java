package it.cloudpanel.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;

public class BulkUploader {

	public static void main(String[] args) {
		try {
			CsvReader reader = new CsvReader("resources\\domande.csv");
			reader.readHeaders();
			while (reader.readRecord()){
				String currDomanda = reader.get("Domande");
				System.out.println(currDomanda);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static ArrayList<String> getDomandeFromCSV() {
		ArrayList<String> result = new ArrayList<String>();

		CsvReader reader;
		try {
			InputStream file = BulkUploader.class.getResourceAsStream("domande.csv");
			reader = new CsvReader(file, Charset.forName("UTF-8"));
//			reader = new CsvReader("resources/domande.csv");
			reader.readHeaders();
			while (reader.readRecord()){
				String currDomanda = reader.get("Domande");
				result.add(currDomanda);
				System.out.println(currDomanda);
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}


}
