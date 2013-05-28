package it.pipe.filters;

import it.pipe.core.Filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RemoveRegex extends Filter {


	public RemoveRegex(String modName, String conFilePath) {
		super(modName, conFilePath);
	}

	@Override
	public ArrayList<String> getOutput(ArrayList<String> input) {

		ArrayList<String> result = new ArrayList<String>();
				
		for (int i = 0; i < input.size(); i++){
			String currToken = input.get(i);

			Collection<String> keys = config.values();
			Iterator<String> iter = keys.iterator();
			boolean match = false;
			while(iter.hasNext()){
				String currValue = iter.next();
				if (currToken.matches(currValue)) {
					match = true;//result.add(currToken);
					//System.out.println("Removed: " + currToken);
				}
			}
			if (!match){
				result.add(currToken);
			}
		}
		System.out.println("\t-> Rimossi " + (input.size() - result.size()) + " items");
		return result;
	}

	
}