
package com.dhiva.server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ServerApp {
	// create a thread queue of a size
	static Queue<ClientProcessingRunnable> threadPool = new LinkedList<>();
	static ClientConnectionsRunnable connectionObj;
	static int portNum;
	static String rootDirectory;
	
	public static void main(String[] args) {
		int numberOfThreads = 5;
		
		if (args.length == 3) {
			numberOfThreads = Integer.parseInt(args[0]);
			portNum = Integer.parseInt(args[1]);
			rootDirectory = args[2];
		} else {
			System.out.println("Please enter the following arguments. <# of processing threads> <port> <rootdir>");
		}
		
		if(rootDirectory.endsWith("/")){
			rootDirectory = rootDirectory.substring(0,rootDirectory.length()-1);
		}
		
		// spans the client processing threads
		spanProcessingThreads(numberOfThreads);
		// spans a new thread to accept client connections
		spanThreadToAcceptConnections();

		while (true) {
			Scanner reader = new Scanner(System.in);
			System.out.println("Enter #1 to shutdown all threads #2 to restart client processing threads #3 shut down server");
			int option = reader.nextInt();
			if (option == 1) {
				stopThreads();
			} else if (option == 2) {
				spanProcessingThreads(numberOfThreads);
				spanThreadToAcceptConnections();
			} else if (option == 3) {
				stopThreads();
				break;
			}
		}

	}

	public static void spanProcessingThreads(int numberOfThreads) {

		while (numberOfThreads >= 0) {
			ClientProcessingRunnable clientObj = new ClientProcessingRunnable();
			clientObj.setName("processor" + numberOfThreads);
			clientObj.getRootDirectory(rootDirectory);
			Thread thread = new Thread(clientObj);
			// assign a runnable class to a thread
			// this runnable class performs the processing needed by the client
			threadPool.add(clientObj);
			thread.start();
			numberOfThreads--;
		}
	}

	public static void spanThreadToAcceptConnections() {
		try {
			connectionObj = new ClientConnectionsRunnable();
			Thread connection = new Thread(connectionObj);
			connection.start();
			connectionObj.getPortNumber(portNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopThreads() {
		for (ClientProcessingRunnable t : threadPool) {
			t.setRunnableState(true);
		}
		connectionObj.setRunnableState(true);
	}
}
