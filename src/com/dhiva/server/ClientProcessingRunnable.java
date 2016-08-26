package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClientProcessingRunnable implements Runnable {
	// instance variable
	private String name;
	private boolean runnableState = false;
	private String rootDirectory;
	static boolean append = true;
	private String requestType;
	private String requestFile;

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

	public void getRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;

	}

	public void setrequestType(String requestType) {
		this.requestType = requestType;
	}

	public void setrequestFile(String requestFile) {
		this.requestFile = requestFile;

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
		OutputStream stream = currentClient.getOutputStream();
		Thread.sleep(3000);

		StringBuffer lines = parseClientRequest(currentClient);

		sendClientFile(currentClient, lines);

		String message = "hello from my web server; processed by" + this.name;
		byte[] data = message.getBytes();
		stream.write(data);
		currentClient.close();
	}

	private void sendClientFile(Socket currentClient, StringBuffer lines) {
		String request = lines.toString();
		String[] splited = request.split("\\s+");
		String requestType = splited[0];
		String requestFile = splited[1];
		if (requestType.equalsIgnoreCase("HEAD")) {

		} else if (requestType.equalsIgnoreCase("GET")) {

			final String FILE_TO_SEND = rootDirectory + requestFile;
			File myFile = new File(FILE_TO_SEND);
			if (!myFile.exists()) {
				String msg = "Sorry file not found";
				byte[] data = msg.getBytes();
				try {
					OutputStream stream = currentClient.getOutputStream();
					stream.write(data);
					stream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (myFile.exists() && myFile.isFile()) {
				byte[] mybytearray = new byte[(int) myFile.length()];
				try {
					OutputStream stream = currentClient.getOutputStream();
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
					bis.read(mybytearray, 0, mybytearray.length);

					System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
					stream.write(mybytearray, 0, mybytearray.length);
					stream.flush();
					bis.close();
					System.out.println("Done.");
				} catch (IOException e) {
					e.printStackTrace();
				}
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
						String displayName = "\">"+fileName;
						String link = startTag +myFile.getName()+"/"+ fileName + displayName + endTag;
						System.out.println(link);
						byte[] data = link.getBytes();
						stream.write(data);
						stream.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private StringBuffer parseClientRequest(Socket currentClient) {
		StringBuffer str = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(currentClient.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() == 0)
					break;
				str.append(line);
				str.append(" ");
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

}
