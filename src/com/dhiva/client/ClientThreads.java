package com.dhiva.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThreads implements Runnable {

	@Override
	public void run() {
		Socket clientListener;
		try {
			clientListener = new Socket("127.0.0.1", 3000);
			InputStream serverData = clientListener.getInputStream();
			BufferedReader reader = new BufferedReader( new InputStreamReader(serverData)); 
			System.out.println(reader.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
