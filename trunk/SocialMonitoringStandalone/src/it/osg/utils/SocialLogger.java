package it.osg.utils;

import it.queue.QueueLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

public class SocialLogger {

	public static void setup(Level level) {
		LogManager.getLogManager().getLogger("").setLevel(level);
		Handler[] handlers = LogManager.getLogManager().getLogger("").getHandlers();
		for ( int index = 0; index < handlers.length; index++ ) {
			handlers[index].setLevel(level);
		}
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("logs/" + QueueLogger.class.getName() + ".log", 5242880, 10, true);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter); 
			LogManager.getLogManager().getLogger("").addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
