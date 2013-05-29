package test;

import it.osg.service.model.Graph;
import it.pipe.core.PipeBlock;
import it.pipe.core.PipelineEngine;
import it.pipe.filters.RemoveRegex;
import it.pipe.filters.RemoveWordList;
import it.pipe.transformers.FrequencyTransformer;
import it.pipe.transformers.Tokenizer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	public static void main(String[] args) {
		String txt="a: PD voti 521.880 2013 Roma: PD voti 267.605 (-254.275 voti) = - 48% 2008 Roma: PDL voti 559.559 2013 Roma: PDL voti 195.749 (-363.810 voti) = - 65% 2008 Roma: M5S voti 40.473 2013 Roma: M5S voti 130.635 (+90.162 voti) = + 222% 2011 Siena: PD voti 11.723 2013 Siena: PD voti 6.483 (-5.240 voti) = - 44% 2011 Siena: M5S voti 833 2013 Siena: M5S voti 2.194 (+ 1.361 voti) = +163% 2009 Ancona PD: voti 15.885 2013 Ancona PD: voti 10.652 (-5.233 voti) = -33% 2009 Ancona PDL: voti 17.432 2013 Ancona PDL: voti 4.266 (-13.166 voti) = -75% 2009 Ancona M5S: voti 2.625 2013 Ancona M5S: voti 5.711 (+ 3.086 voti)= +117% 2009 Avellino PD: voti 10.324 2013 Avellino PD: voti 6.845 (- 3.479 voti) = -33% 2009 Avellino PDL: voti 5.043 2013 Avellino PDL: 2.513 (- 2.530 voti ) = -50% 2009 Avellino M5S: voti 0 2013 Avellino M5S: 1.054 (+ 1.054) = + MESSAGE: Rivedi l'intervista integrale COMMENT: Me la riguarderò sicuramente Floris ... stai tranquillo proprio stamattina appena alzato prima di guardare le offerte di lavoro mi dicevo chissà se Floris ha rimesso in onda quella interessantissima intervista ... insomma la aspettavo proprio ... COMMENT: I dati che non hanno mostrato delle comunali 2013: 2008 Roma: PD voti 521.880 2013 Roma: PD voti 267.605 (-254.275 voti) = - 48% 2008 Roma: PDL voti 559.559 2013 Roma: PDL voti 195.749 (-363.810 voti) = - 65% 2008 Roma: M5S voti 40.473 2013 Roma: M5S voti 130.635 (+90.162 voti) = + 222% 2011 Siena: PD voti 11.723 2013 Siena: PD voti 6.483 (-5.240 voti) = - 44% 2011 Siena: M5S voti 833 2013 Siena: M5S voti 2.194 (+ 1.361 voti) = +163% 2009 Ancona PD: voti 15.885 2013 Ancona PD: voti 10.652 (-5.233 voti) = -33% 2009 Ancona PDL: voti 17.432 2013 Ancona PDL: voti 4.266 (-13.166 voti) = -75% 2009 Ancona M5S: voti 2.625 2013 Ancona M5S: voti 5.711 (+ 3.086 voti)= +117% 2009 Avellino PD: voti 10.324 2013 Avellino PD: voti 6.845 (- 3.479 voti) = -33% 2009 Avellino PDL: voti 5.043 2013 Avellino PDL: 2.513 (- 2.530 voti ) = -50% 2009 Avellino M5S: voti 0 2013 Avellino M5S: 1.054 (+ 1.054)";
		ArrayList<String> input = new ArrayList<String>();
		input.add(txt);
		
		PipelineEngine eng = new PipelineEngine();
		ArrayList<PipeBlock> blocks = new ArrayList<PipeBlock>();
		Tokenizer block1 = new Tokenizer("Tokenizer", null);
		blocks.add(block1);
		RemoveWordList block2 = new RemoveWordList("RemoveWordList", null);
		block2.addConfiguration("wordListPath", "stopwords_it.csv");
		blocks.add(block2);
		RemoveRegex block3 = new RemoveRegex("RemoveRegex", null);
		block3.addConfiguration("regex1", "^[0-9]+");
		block3.addConfiguration("regex2", "(\\d+)(\\.)(\\d+)");
		blocks.add(block3);
		FrequencyTransformer block4 = new FrequencyTransformer("FrequencyTransformer", null);
		blocks.add(block4);
		
		ArrayList<String> pipeResult = eng.run(blocks, input);
		
		String re1="(\\d+)";	// Integer Number 1
	    String re2 = "^[0-9]+";
	    String re3 = "(\\d+)(\\s+)";

	    Pattern p = Pattern.compile(re1,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m = p.matcher(txt);
	    if (m.find()) {
	        String int1=m.group(1);
	        System.out.print("("+int1.toString()+")"+"\n");
	    }
	    
	    if (txt.matches(re1)) {
	    	System.out.println("MATCH OK");
	    }
	    
	    if (txt.matches(re2)) {
	    	System.out.println("MATCH OK");
	    }
	    
	    if (txt.matches(re3)) {
	    	System.out.println("MATCH OK");
	    }
	    
	}
	
}
