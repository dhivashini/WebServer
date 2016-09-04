package com.dhiva.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadSocketForParsing {
	private Socket currentClient;
	private StringBuffer clientRequest;

	public ReadSocketForParsing(Socket currentClient) {
		this.currentClient = currentClient;
	}

	public StringBuffer readrequest() {
		try {
			clientRequest = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(currentClient.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() == 0)
					break;
				clientRequest.append(line);
				clientRequest.append(" ");
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientRequest;
	}
}
