package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

import it.osg.psar.FacebookPSARMultiThreadPost;

public class PerformaceTestMultiThread {

	public static void main(String[] args) {
		
		System.getProperties().put("http.proxyHost", "proxy.gss.rete.poste");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("http.proxyUser", "rete\\servill7");
		System.getProperties().put("http.proxyPassword", "Paolos10");
		
		CsvWriter outWriter = openOutputFile("resources/resultMT_" + System.currentTimeMillis() +".csv");
		
		for (int i = 1; i <= 20; i++) {
			int numThreads = i*10;
			String numThreadsString = String.valueOf(numThreads);
			long start = System.currentTimeMillis();
			FacebookPSARMultiThreadPost.main(new String[]{numThreadsString});
			long end = System.currentTimeMillis();
			long elapsedTime = (end - start)/1000;
			System.out.println("ELAPSED: " + elapsedTime);
			try {
				outWriter.write(numThreadsString);
				outWriter.write(String.valueOf(elapsedTime));
				outWriter.endRecord();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
		outWriter.close();
		
	}
	
	
	private static CsvWriter openOutputFile(String outFile) {

		boolean alreadyExists = new File(outFile).exists();

		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outFile, true), ',');

			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)	{
				csvOutput.write("NumThread");
				csvOutput.write("Time");
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			return csvOutput;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
}
