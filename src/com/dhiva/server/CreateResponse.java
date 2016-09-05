package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dhiva.server.HttpRequest.HttpMethod;

public class CreateResponse {
	private HttpRequest requestObj;

	public CreateResponse(HttpRequest requestObj, HttpMethod methodObj) {
		this.requestObj = requestObj;
	}
	
	public void createResponseBody() {
		// Check for HTTP Version
		String httpVersion = requestObj.getHttpVersion();
		String methodName = requestObj.getHttpMethod();
		if (!httpVersion.equals("HTTP/1.1") && !httpVersion.equals("HTTP/1.0")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Check for bad method name
		if (methodName.equals("BAD")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Methods not implemented - send 501
		if (!methodName.equals("GET") && !methodName.equals("HEAD") && !methodName.equals("POST")) {
			String statusCode = "501 Not Implemented";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Check for illegal access
		String resourceURI = requestObj.getResourceURI();
		if (resourceURI.contains("..")) {
			String statusCode = "403 Access Forbidden";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		if (!resourceURI.startsWith("/")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		String requestFile = requestObj.getResourceURI();
		final String FILE_TO_SEND = rootDirectory + requestFile;
		File myFile = new File(FILE_TO_SEND);
		try {
			if (!myFile.exists()) {
				String statusCode = "404 Not Found";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
				return;
			} else if (myFile.exists() && myFile.isFile()) {
				byte[] mybytearray = new byte[(int) myFile.length()];
				//OutputStream stream = currentClient.getOutputStream();
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
				bis.read(mybytearray, 0, mybytearray.length);
//				String s = "HTTP/1.1 200 \r\nContent-Type: application/pdf\r\nConnection: close\r\n\r\n";
//				byte[] data = s.getBytes();
//				stream.write(data);
//				stream.write(mybytearray, 0, mybytearray.length);
//				stream.flush();
				bis.close();
				this.responseBody		= mybytearray;
				this.statusCode 		= "200 OK";
				this.contentLength 		= (int)mybytearray.length;
				this.contentType		= setContentType(resourceURI);
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
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
	public void createResponseHeader() {
		// TODO Auto-generated method stub
		
	}
	public void createResponseBody() {
		// TODO Auto-generated method stub
		
	}
}
