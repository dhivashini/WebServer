package com.dhiva.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientProcessingRunnable implements Runnable {
	// instance variable
	private String name;
	private boolean runnableState = false;

	// setter and getter to access the private fields
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRunnableState(boolean isStopped) {
		this.runnableState = isStopped;
	}

	@Override
	public void run() {
		try {
			while (true && !runnableState) {
				// each thread getting a client socket in a synchronized manner
				// since the synchronization is done on the method
				Socket currentClient = ClientConnectionsRunnable.getClientSocket();
				// if the is no client request,sleep and continue to check for
				// client connections
				if (currentClient == null) {
					Thread.sleep(50);
					continue;
				} else {
					processClientRequest(currentClient);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void processClientRequest(Socket currentClient) throws InterruptedException, IOException {
		System.out.println("processing on thread ");
		Thread.sleep(3000);
		OutputStream stream = currentClient.getOutputStream();
		String message = "hello from server; processed by" + this.name;
		byte[] data = message.getBytes();
		stream.write(data);
		currentClient.close();
	}
}
