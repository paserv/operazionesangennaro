package it.queue;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class QueueLogger {

	public static void setup(Level level) {
		LogManager.getLogManager().getLogger("").setLevel(level);
		Handler[] handlers = LogManager.getLogManager().getLogger("").getHandlers();
		for ( int index = 0; index < handlers.length; index++ ) {
			handlers[index].setLevel(level);
		}
	}
	
}
