
package com.dhiva.server;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ServerApp {

	static int numberOfThreads;

	public static void main(String[] args) {
		if (args.length > 0) {
			numberOfThreads = Integer.parseInt(args[0]);
		}
		
		int i = 1;
		// create a thread queue of a size
		Queue<Thread> threadPool = new LinkedList<>();
		while (i < numberOfThreads) {
			ClientProcessingRunnable clientObj = new ClientProcessingRunnable();
			clientObj.setName("processor" + i);
			Thread thread = new Thread(clientObj);
			// assign a runnable class to a thread
			// this runnable class performs the processing needed by the client
			threadPool.add(thread);
			thread.start();
			i++;
		}
		
		try {
			ClientConnectionsRunnable connectionObj = new ClientConnectionsRunnable();
			Thread connection = new Thread(connectionObj);
			connection.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
