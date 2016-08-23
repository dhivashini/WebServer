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

public class ClientProcessingRunnable implements Runnable {
	// instance variable
	private String name;
	private boolean runnableState = false;
	private String rootDirectory;

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
		OutputStream stream = currentClient.getOutputStream();
		Thread.sleep(3000);

		ArrayList<String> lines = parseClientRequest(currentClient);

		sendClientFile(currentClient, lines);

		String message = "hello from my web server; processed by" + this.name;
		byte[] data = message.getBytes();
		stream.write(data);
		currentClient.close();
	}

	private void sendClientFile(Socket currentClient, ArrayList<String> lines) {
		String[] splited = lines.get(0).split("\\s+");
		String requestType = splited[0];
		String requestFile = splited[1];
		final String FILE_TO_SEND = rootDirectory + requestFile;
		File myFile = new File(FILE_TO_SEND);
		byte[] mybytearray = new byte[(int) myFile.length()];
		try {
			OutputStream stream = currentClient.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
			bis.read(mybytearray, 0, mybytearray.length);

			System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
			stream.write(mybytearray, 0, mybytearray.length);
			stream.flush();
			System.out.println("Done.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> parseClientRequest(Socket currentClient) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(currentClient.getInputStream()));
			// PrintWriter out = new PrintWriter(stream);
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() == 0)
					break;
				lines.add(line);
				// out.print(line + "\r\n");
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public void getRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;

	}
}
