package com.dhiva.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
		ArrayList<String> lines = new ArrayList<String>();
		
		Thread.sleep(3000);
		OutputStream stream = currentClient.getOutputStream();
		
		// Get input and output streams to talk to the client
        BufferedReader in = new BufferedReader(new InputStreamReader(currentClient.getInputStream()));
        PrintWriter out = new PrintWriter(currentClient.getOutputStream());
       
        String line;
        while ((line = in.readLine()) != null) {
          if (line.length() == 0)
            break;
          lines.add(line);
          out.print(line + "\r\n");
          System.out.println(line);
        }
       
	
		String message = "hello from server; processed by" + this.name;
		byte[] data = message.getBytes();
		stream.write(data);
		currentClient.close();
	}
}
