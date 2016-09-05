package com.dhiva.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.dhiva.server.HttpRequest.HttpMethod;

public class ClientProcessingRunnable implements Runnable {
	// instance variable
	private String name;
	private String rootDirectory;
	private boolean runnableState = false;
	static boolean append = true;

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
	
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getRootDirectory() {
		return rootDirectory;
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
		ReadSocketForParsing socketObj = new ReadSocketForParsing(currentClient);
		StringBuffer clientRequest = socketObj.readrequest();
		HttpRequestParser parseObj = new HttpRequestParser(clientRequest);
		HttpRequest requestObj = parseObj.parse();
		HttpMethod methodObj = parseObj.parseMethod();
		CreateResponse createResponseObj = new CreateResponse(requestObj, methodObj);
		createResponseObj.createResponseBody();
		createResponseObj.createResponseHeader();
		HttpResponse responseObj = new HttpResponse();
		sendClientFile(currentClient, responseObj);
		currentClient.close();
	}

	private void sendClientFile(Socket currentClient, HttpResponse responseObj) {
		String responseBody = responseObj.getResponseBody();
		String responseHeader = responseObj.getResponseHeader();
		String response = responseHeader + responseBody;
		OutputStream stream;
		try {
			stream = currentClient.getOutputStream();
			byte[] clientResponse = response.getBytes();
			stream.write(clientResponse);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
