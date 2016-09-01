package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {
	private byte[] responseHeader;
	private byte[] responseBody; // In Bytes
	private String statusCode;
	private int contentLength;
	private String contentType;
	private String rootDirectory;
	private HttpRequest requestObj;

	public HttpResponse(HttpRequest requestObj) {
		this.requestObj = requestObj;
	}

	public void getRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;

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

	}

	private String setContentType(String resourceURI) {

		return null;
	}

	private void setHeaderFieldsAndCreateHeader(String statusCode2, String htmlBody) {

	}

}
