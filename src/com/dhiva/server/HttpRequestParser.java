package com.dhiva.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequestParser {
	private Socket currentClient;
	private StringBuffer clientRequest;
	public HttpRequestParser(Socket currentClient) {
		this.currentClient = currentClient;
	}

	public HttpRequest parse() {
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
		String request = clientRequest.toString();
		String[] splited = request.split("\\s+");
		HttpRequest requestObj = new HttpRequest();
		requestObj.setHttpMethod(splited[0]);
		requestObj.setResourceURI(splited[1]);
		requestObj.setHttpVersion(splited[2]);
		return requestObj;

	}

}
