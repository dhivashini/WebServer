package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
		HttpResponse responseObj = new HttpResponse(requestObj);
		sendClientFile(currentClient, requestObj);
		currentClient.close();
	}

	private void sendClientFile(Socket currentClient, HttpRequest requestObj) {
		String requestType = requestObj.getHttpMethod();

		String requestVersion = requestObj.getHttpVersion();
		if (requestType.equalsIgnoreCase("HEAD")) {

		} else if (requestType.equalsIgnoreCase("GET")) {

		}
		if (myFile.exists() && myFile.isDirectory()) {
			OutputStream stream;
			try {
				stream = currentClient.getOutputStream();
				File[] listOfFiles = myFile.listFiles();
				List<String> results = new ArrayList<String>();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						results.add(file.getName());
					}
				}
				for (String fileName : results) {
					String startTag = "<!DOCTYPE html> <html> <body><a href=\"";
					String endTag = "</a></body> </html>";
					String displayName = "\">" + fileName;
					String link = startTag + myFile.getName() + "/" + fileName + displayName + endTag;
					System.out.println(link);
					String s = "HTTP/1.1 200 \r\nContent-Type: text/html\r\nConnection: close\r\n\r\n";
					byte[] data1 = s.getBytes();
					stream.write(data1);
					byte[] data = link.getBytes();
					stream.write(data);
					stream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
}
