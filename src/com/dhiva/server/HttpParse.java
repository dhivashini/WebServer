package com.dhiva.server;

import java.io.File;

public class HttpParse {
	private StringBuffer clientRequest;
	
	public HttpParse(StringBuffer request) {
		this.clientRequest = request;
	}

	public void parse() {
		String request = clientRequest.toString();
		String[] splited = request.split("\\s+");
		String requestType = splited[0];
		String requestFile = splited[1];
		String requestVersion = splited[2];
		ClientProcessingRunnable obj = new ClientProcessingRunnable();
		obj.setrequestType(requestType);
		obj.setrequestFile(requestFile);
	}

}
