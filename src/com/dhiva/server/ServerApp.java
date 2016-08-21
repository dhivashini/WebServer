
package com.dhiva.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class ServerApp {
	
	//create a collection to store the client connections
	static Vector<Socket> listofClients = new Vector<>();
	
	//synchronize on the collection of clients, 
	//so that only one thread is able to remove that particular client
	public synchronized static Socket getClientSocket(){
		if (!listofClients.isEmpty()) {
			return listofClients.remove(0);
		} else
			return null;
	}

	public static void main(String[] args) {
		ServerSocket socketListener = null;
		int i = 0;
		//create a thread queue of a size
		Queue<Thread> threadPool = new LinkedList<>();
		while (i <= 5) {
			ClientProcessingRunnable clientObj = new ClientProcessingRunnable();
			clientObj.setName("processor"+i);
			Thread thread = new Thread(clientObj);
			//assign a runnable class to a thread
			//this runnable class performs the processing needed by the client
			threadPool.add(thread);
			thread.start();
			i++;
		}
		try {
			i=0;
			//listen on a particular port
			socketListener = new ServerSocket(3000);
			while (true) {
				//accept multiple connections on that port
				//creating a socket for each new client request
				Socket clientSocket = socketListener.accept();
				i++;
				//add each client socket to the vector
				listofClients.add(clientSocket);
				System.out.println("accepted a client number " + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
