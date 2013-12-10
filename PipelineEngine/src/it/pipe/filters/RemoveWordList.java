package it.pipe.filters;

import it.pipe.core.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class RemoveWordList extends Filter {


	public RemoveWordList(String modName, String conFilePath) {
		super(modName, conFilePath);
	}

	@Override
	public ArrayList<String> getOutput(ArrayList<String> input) {

		ArrayList<String> result = new ArrayList<String>();
		Hashtable<String, String> wordList = this.getWordList();

		for (int i = 0; i < input.size(); i++){
			String currToken = input.get(i);

			String vocabMatch = wordList.get(currToken.toLowerCase());
			if (vocabMatch == null) {
				result.add(currToken);
			}

		}
		System.out.println("\t-> Rimossi " + (input.size() - result.size()) + " items");
		return result;
	}


	private Hashtable<String, String> getWordList() {
		Hashtable<String, String> vocabularyMap = new Hashtable<String, String>();

		Collection<String> keys = config.values();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()){
			String currValue = iter.next();

			try {


				File dictionary = new File("resources/" + currValue);
				Scanner sc = new Scanner(dictionary);

				while (sc.hasNext()) {
					String currTerm = sc.nextLine();
					String[] spl = currTerm.split(" ");
					vocabularyMap.put(spl[0].toLowerCase(), spl[0].toLowerCase());
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return vocabularyMap;
	}


}
