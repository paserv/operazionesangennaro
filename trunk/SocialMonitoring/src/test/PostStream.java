package test;

import facebook4j.Post;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class PostStream {

	public static void main(String[] args) {
		Hashtable<String, String> sindaci = new Hashtable<String, String>();

//		sindaci.put("Ignazio Marino","63360159216");
//		sindaci.put("Leonardo Michelini","390316297742803");
//		sindaci.put("Giuliano Pisapia","112352038802143");
//		sindaci.put("Oreste Perri","68340403104");
//		sindaci.put("Emilio Del Bono","174668952677211");
//		sindaci.put("Virginio Brivio","489219834450153");
//		sindaci.put("Alcide Molteni","434003210024874");
//		sindaci.put("Mario Lucini","206379842757683");
//		sindaci.put("Attilio Fontana","354028671345304");
//		sindaci.put("Maria Rita Rossa","192356270892210");
//		sindaci.put("Andrea Ballare","133810866691494");
//		sindaci.put("Luigi De Magistris","61657367059");
//		sindaci.put("Vincenzo De Luca","53388213256");
//		sindaci.put("Pio Del Gaudio","243230429056389");
//		sindaci.put("Umberto Di Primio","118830804861699");
//		sindaci.put("Virginio Merola","173780875979272");
//		sindaci.put("Fabrizio Matteucci","161427010581901");
//		sindaci.put("Federico Pizzarotti","258372870902870");
//		sindaci.put("Graziano Delrio","160694770110");
//		sindaci.put("Roberto Balzani","313671835390305");
//		sindaci.put("Andrea Gnassi","128729313852232");
//		sindaci.put("Michele Emiliano","53505098083");
//		sindaci.put("Pasquale Cascella","376095029171963");
//		sindaci.put("Paolo Perrone","36630618044");
//		sindaci.put("Roberto Cosolini","250216921658628");
//		sindaci.put("Romano Carancini","278376212226141");
//		sindaci.put("Luca Ceriscioli","118672271588789");
//		sindaci.put("Massimo Zedda","70036951368");
//		sindaci.put("Gianfranco Ganau","31770497795");
//		sindaci.put("Wladimiro Boccali","129919073737485");
//		sindaci.put("Leopoldo Di Girolamo","49423676393");
//		sindaci.put("Sergio Abramo","343365372377122");
//		sindaci.put("Mario Occhiuto","149864881742810");
//		sindaci.put("Luigi Brasiello","110407065829834");
//		sindaci.put("Leoluca Orlando","422625477753021");
//		sindaci.put("Federico Piccitto","526504600745221");
//		sindaci.put("Renato Accorinti","49957446932");
//		sindaci.put("Giancarlo Garozzo","469857859741998");
//		sindaci.put("Vincenzo Bianco","109633709120808");
//		sindaci.put("Marco Doria","234736036582930");
//		sindaci.put("Carlo Capacci","486046941469232");
//		sindaci.put("Massimo Federici","529405893749379");
//		sindaci.put("Matteo Renzi","113335124914");
//		sindaci.put("Marco Filippeschi","270956182996934");
//		sindaci.put("Alessandro Tambellini","235766926498784");
//		sindaci.put("Samuele Bertinelli","327284627283068");
//		sindaci.put("Emilio Bonifazi","121112761293626");
//		sindaci.put("Alessandro Volpi","200303126695453");
//		sindaci.put("Roberto Cenni","180764825332055");
//		sindaci.put("Jacopo Massaro","352604201451113");
//		sindaci.put("Giovanni Manildo","420895527988113");
//		sindaci.put("Achille Variati","478212418874107");
//		sindaci.put("Flavio Zanonato","32377918793");
//		sindaci.put("Flavio Tosi","553261914706917");
//		sindaci.put("Paolo Lucchi","544192248936152");
		
		
		sindaci.put("Simone Uggetti","120577671459080");
		sindaci.put("Piero Franco Rodolfo Fassino","56221866936");
		sindaci.put("Federico Borgna","364947573545455");
		sindaci.put("Fausto Pepe","175061545874587");
		sindaci.put("Furio Honsell","534334929944459");
		sindaci.put("Luigi Spagnolli","120435464635776");
		sindaci.put("Claudio Pedrotti","164305146951493");	
		sindaci.put("Guido Tendas","354956144525858");
		sindaci.put("Vito Damiano","207112332753505");
		sindaci.put("Bruno Valentini","457562410985448");
		sindaci.put("Giorgio Orsoni","598261373517812");
		sindaci.put("Ivo Rossi","1503811910");
		sindaci.put("Guido Castelli","52932694036");

		Date from = null;
		Date to = null;
		try {
			from = DateUtils.parseDateAndTime("01-10-2013 00:00:00");
			to = DateUtils.parseDateAndTime("31-10-2013 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Enumeration<String> keys = sindaci.keys();
		while(keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			String currId = sindaci.get(currKey);

			ArrayList<Post> posts = FacebookUtils.getAllPosts(currId, from, to, null);
			Iterator<Post> postIter = posts.iterator();
			while (postIter.hasNext()) {
				Post currPost = postIter.next();
				if (currPost.getMessage() != null) {
					System.out.print(currKey + "," + currId + "," + DateUtils.formatDate(currPost.getCreatedTime()) + ",");
					if (currPost.getFrom().getId().toString() != null && currPost.getFrom().getId().toString().equalsIgnoreCase(currId)) {
						System.out.println("OWN_POST");
					} else {
						System.out.println("FAN_POST");
					}
				}
			}
		}


	}

}
