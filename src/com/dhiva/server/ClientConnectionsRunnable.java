package com.dhiva.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ClientConnectionsRunnable implements Runnable {
	// create a collection to store the client connections
	static Vector<Socket> listofClients = new Vector<>();
	ServerSocket socketListener = null;
	private boolean runnableState=false;
	private int portNumber;

	// synchronize on the collection of clients,
	// so that only one thread is able to remove that particular client
	public synchronized static Socket getClientSocket() {
		if (!listofClients.isEmpty()) {
			return listofClients.remove(0);
		} else
			return null;
	}
	
	public void setRunnableState(boolean isStopped) {
		this.runnableState=isStopped;
		
		try {
			socketListener.close();
		} catch (IOException e) {
		//	e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int i = 0;
		try {
			// listen on a particular port
			socketListener = new ServerSocket(portNumber);

			while (true && !runnableState) {

				// accept multiple connections on that port
				// creating a socket for each new client request
				Socket clientSocket = socketListener.accept(); 
				i++;
				// add each client socket to the vector
				listofClients.add(clientSocket);
				System.out.println("accepted a client number " + i);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println();
		}

	}

	public void getPortNumber(int portNum) {
		this.portNumber = portNum;
	}

	

}
