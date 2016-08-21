package com.dhiva.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ClientConnectionsRunnable implements Runnable {
	// create a collection to store the client connections
	static Vector<Socket> listofClients = new Vector<>();
	ServerSocket socketListener = null;

	// synchronize on the collection of clients,
	// so that only one thread is able to remove that particular client
	public synchronized static Socket getClientSocket() {
		if (!listofClients.isEmpty()) {
			return listofClients.remove(0);
		} else
			return null;
	}

	@Override
	public void run() {
		int i = 0;
		try {
			// listen on a particular port
			socketListener = new ServerSocket(3000);
			while (true) {
				// accept multiple connections on that port
				// creating a socket for each new client request
				Socket clientSocket = socketListener.accept();
				i++;
				// add each client socket to the vector
				listofClients.add(clientSocket);
				System.out.println("accepted a client number " + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
