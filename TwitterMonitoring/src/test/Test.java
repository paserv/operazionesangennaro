package test;

import java.util.Enumeration;
import java.util.Hashtable;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class Test {


	public static void main(String[] args) throws TwitterException {

		Hashtable<String, String> input = new Hashtable<String, String>();
		
		input.put("Ignazio Marino","ignaziomarino");
		input.put("Simone Petrangeli","simopetrangeli");
		input.put("Nicola Ottaviani","OttavianiNicola");
		input.put("Giovanni Di Giorgi","gdigiorgi");
		input.put("Leonardo Michelini","Viterbo2013");
		input.put("Giuliano Pisapia","giulianopisapia");
		input.put("Franco Tentorio","");
		input.put("Oreste Perri","");
		input.put("Nicola Sodano","");
		input.put("Alessandro Cattaneo","aleSindaco");
		input.put("Emilio Del Bono","delbono2013");
		input.put("Virginio Brivio","virginiobrivio");
		input.put("Alcide Molteni","");
		input.put("Mario Lucini","MarioLuciniComo");
		input.put("Simone Uggetti","");
		input.put("Roberto Scanagatti","");
		input.put("Attilio Fontana","attilio_fontana");
		input.put("Piero Franco Rodolfo Fassino","pierofassino");
		input.put("Maria Rita Rossa","");
		input.put("Federico Borgna","FedericoBorgna");
		input.put("Fabrizio Brignolo","FabriBrignolo");
		input.put("Andrea Ballarè","BallareSindaco");
		input.put("Andrea Corsaro","");
		input.put("Donato Gentile","");
		input.put("Luigi De Magistris","demagistris");
		input.put("Paolo Foti","Fotisindaco");
		input.put("Fausto Pepe","fausto_pepe");
		input.put("Vincenzo De Luca","vincenzodeluca");
		input.put("Pio Del Gaudio","Pio_DelGaudio");
		input.put("Massimo Cialente","mcialente");
		input.put("Umberto Di Primio","udiprimio");
		input.put("Maurizio Brucchi","");
		input.put("Luigi Albore Mascia","");
		input.put("Virginio Merola","virginiomerola");
		input.put("Giorgio Pighi","");
		input.put("Fabrizio Matteucci","ravennaditutti");
		input.put("Tiziano Tagliani","TizianoTagliani");
		input.put("Federico Pizzarotti","SindacoParma");
		input.put("Graziano Delrio","graziano_delrio");
		input.put("Roberto Balzani","BalzaniRoberto");
		input.put("Paolo Dosi","");
		input.put("Andrea Gnassi","andreagnassi");
		input.put("Michele Emiliano","micheleemiliano");
		input.put("Giovanni Battista Mongelli","sindacofoggia");
		input.put("Pasquale Cascella","cascellasindaco");
		input.put("Nicola Giorgino","NicolaGiorgino");
		input.put("Luigi Nicola Riserbato","");
		input.put("Paolo Perrone","Paolo_Perrone");
		input.put("Cosimo Consales","");
		input.put("Ippazio Stefano","");
		input.put("Alessandro Andreatta","");
		input.put("Luigi Spagnolli","LuigiSpagnolli");
		input.put("Vito Santarsiero","VitoSantarsiero");
		input.put("Salvatore Adduce","adducesocial");
		input.put("Roberto Cosolini","RobertoCosolini");
		input.put("Ettore Romoli","EttoreRomoli");
		input.put("Furio Honsell","FURIOHONSELL");
		input.put("Claudio Pedrotti","claudiopedrotti");
		input.put("Valeria Mancinelli","");
		input.put("Romano Carancini","");
		input.put("Guido Castelli","sindacoascoli");
		input.put("Luca Ceriscioli","");
		input.put("Nella Brambatti","");
		input.put("Massimo Zedda","massimozedda");
		input.put("Giovanni Maria Enrico Giovannelli","");
		input.put("Romeo Frediani","");
		input.put("Gianfranco Ganau","gfganau");
		input.put("Giuseppe Casti","giuseppe_casti");
		input.put("Emilio Agostino Gariazzo","");
		input.put("Guido Tendas","");
		input.put("Domenico Lerede","");
		input.put("Davide Ferreli","");
		input.put("Alessandro Bianchi","");
		input.put("Alessandro Collu","");
		input.put("Teresa Maria Pani","");
		input.put("Wladimiro Boccali","");
		input.put("Leopoldo Di Girolamo","");
		input.put("Sergio Abramo","SergioAbramo");
		input.put("Giuseppe Castaldo","");
		input.put("Mario Occhiuto","");
		input.put("Nicola D'Agostino","");
		input.put("Peppino Vallone","");
		input.put("Luigi Di Bartolomeo","");
		input.put("Luigi Brasiello","gigibrasiello");
		input.put("Leoluca Orlando","LeolucaOrlando1");
		input.put("Marco Zambuto","");
		input.put("Paolo Garofalo","");
		input.put("Federico Piccitto","FedePiccitto");
		input.put("Michele Campisi","");
		input.put("Renato Accorinti","RenatoAccorinti");
		input.put("Giancarlo Garozzo","");
		input.put("Vincenzo Bianco","");
		input.put("Vito Damiano","");
		input.put("Bruno Giordano","BrunoGiordano54");
		input.put("Marco Doria","DoriaMarco");
		input.put("Federico Berruti","");
		input.put("Carlo Capacci","carlocapacci");
		input.put("Massimo Federici","FedericiMassimo");
		input.put("Matteo Renzi","matteorenzi");
		input.put("Giuseppe Fanfani","");
		input.put("Alessandro Cosimi","");
		input.put("Marco Filippeschi","Filippeschi");
		input.put("Bruno Valentini","");
		input.put("Alessandro Tambellini","");
		input.put("Samuele Bertinelli","");
		input.put("Emilio Bonifazi","EmilioBonifazi");
		input.put("Angelo Andrea Zubbani","");
		input.put("Alessandro Volpi","ale_volpi");
		input.put("Roberto Cenni","Roberto_Cenni");
		input.put("Giorgio Orsoni","Giorgio_Orsoni");
		input.put("Jacopo Massaro","JacopoMassaro");
		input.put("Giovanni Manildo","GiovanniManildo");
		input.put("Achille Variati","");
		input.put("Flavio Zanonato","flaviozanonato");
		input.put("Bruno Piva","");
		input.put("Flavio Tosi","conflaviotosi");
		
		Twitter twitter = TwitterFactory.getSingleton();
		
		System.out.println("Nome;Followers;Favourites;Friends;CreatedAt;ListedCount;StatusesCount");
		
		Enumeration<String> keys = input.keys();
		while (keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			String currID = input.get(currKey);
			if (!currID.equalsIgnoreCase("")) {
				User us = twitter.showUser(currID);
				System.out.println(currKey + ";" + us.getFollowersCount() + ";" + us.getFavouritesCount() + ";" + us.getFriendsCount() + ";" + us.getCreatedAt() + ";" + us.getListedCount() + ";" + us.getStatusesCount());
			}
			
		}
		

	}
}
