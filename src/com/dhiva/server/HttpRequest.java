package com.dhiva.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequest {

	enum HttpMethod {
		GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT, BAD
	};

	private HttpMethod httpMethod;
	private String requestFile;
	private String requestVersion;
	private Socket currentClient;
	private StringBuffer clientRequest;

	public HttpRequest(Socket currentClient) {
		this.currentClient = currentClient;
	}

	public void parse() {
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
		this.setHttpMethod(splited[0]);
		this.setRequestFile(splited[1]);
		this.setRequestVersion(splited[2]);

	}

	private void setRequestVersion(String requestVersion) {
		this.requestVersion = requestVersion;
	}

	private void setRequestFile(String requestFile) {
		this.requestFile = requestFile;
	}

	public void setHttpMethod(String requestType) {
		String httpMethod = requestType;
		if (httpMethod.equals("GET")) {
			this.httpMethod = HttpMethod.GET;
		} else if (httpMethod.equals("HEAD")) {
			this.httpMethod = HttpMethod.HEAD;
		} else if (httpMethod.equals("POST")) {
			this.httpMethod = HttpMethod.POST;
		} else if (httpMethod.equals("PUT")) {
			this.httpMethod = HttpMethod.PUT;
		} else if (httpMethod.equals("DELETE")) {
			this.httpMethod = HttpMethod.DELETE;
		} else if (httpMethod.equals("TRACE")) {
			this.httpMethod = HttpMethod.TRACE;
		} else if (httpMethod.equals("CONNECT")) {
			this.httpMethod = HttpMethod.CONNECT;
		} else {
			this.httpMethod = HttpMethod.BAD;
		}
	}
	
	public String getHttpMethod() {
		return httpMethod.toString();
	}

	public String getRequestFile() {
		return requestFile;
	}

	public String getRequestVersion() {
		return requestVersion;
	}


}