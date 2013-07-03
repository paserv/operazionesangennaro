package test;

import java.util.ArrayList;
import java.util.Iterator;

import it.osg.datasource.facebook.label.PSARData;
import it.osg.service.model.Graph;

public class PSARStandalone {

	public static void main(String[] args) {
		
		System.out.println("ID;mediapost;totpost;totPostFromFan;totPostFromFanMedio;mediaNuoviFan;totNuoviFanlikes;totFan;talkAbout;commentCount;commentsPerPost;uniqueAuthors;uniqueAuthorsPerPost;mediaLikePerPost;totLikes;sharesPerPost;totShares;commentsPerAuthor;numGiorni;");
		
		printResult("56208847866", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("596212503724395", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("112352038802143", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("68340403104", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("489219834450153", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("206379842757683", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("354028671345304", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("192356270892210", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("364947573545455", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("133810866691494", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("61657367059", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("53388213256", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("243230429056389", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("173780875979272", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("161427010581901", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("258372870902870", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("160694770110", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("313671835390305", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("128729313852232", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("53505098083", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("36630618044", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("250216921658628", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("164305146951493", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("278376212226141", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("118672271588789", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("70036951368", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("31770497795", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("354956144525858", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("422625477753021", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("195210777191994", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("234736036582930", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("529405893749379", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("113335124914", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("270956182996934", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("327284627283068", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("121112761293626", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("180764825332055", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("352604201451113", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		printResult("478212418874107", "01-06-2013 00:00:00", "30-06-2013 23:59:59");
		

		
	}
	
	public static void printResult(String id, String from, String to) {
		PSARData result = new PSARData();
		ArrayList<Graph> gr = new ArrayList<Graph>();
		gr.addAll(result.getGraphData(new Object[]{id, from, to}));
		System.out.print(id + ";");
		Iterator<Graph> iter = gr.iterator();
		while (iter.hasNext()) {
			Graph curr = iter.next();
			System.out.print(curr.getOrdinate() + ";");
		}
		System.out.print("\n");
		
		
		
	}
	
}
