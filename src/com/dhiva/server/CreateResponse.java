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
	private HttpMethod methodObj;
	

	public CreateResponse(HttpRequest requestObj, HttpMethod methodObj) {
		this.requestObj = requestObj;
		this.methodObj = methodObj;
	}
	
	public void createResponseBody() {
		// Check for HTTP Version
		String httpVersion = requestObj.getHttpVersion();
		String httpMethod = methodObj.getHttpMethod();
		String resourceURI = requestObj.getResourceURI();
		
		if (!httpVersion.equals("HTTP/1.1") && !httpVersion.equals("HTTP/1.0")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Check for bad method name
		if (httpMethod.equals("BAD")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Methods not implemented - send 501
		if (!httpMethod.equals("GET") && !httpMethod.equals("HEAD") && !httpMethod.equals("POST")) {
			String statusCode = "501 Not Implemented";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			setHeaderFieldsAndCreateHeader(statusCode, htmlBody);
			return;
		}

		// Check for illegal access		
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

		if (httpMethod.equals("GET")){
		final String FILE_TO_SEND = rootDirectory + resourceURI;
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
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		if(this.httpRequest.getHttpMethod().equals("HEAD") && statusCode.equals("200 OK"))
			//			statusCode		= "204 No Content";

			this.statusCode 	= statusCode;
			this.responseBody 	= convertToBytes(htmlBody);
			this.contentType	= "text/html";
			this.contentLength 	= this.responseBody.length;
			this.createResponseHeader();
		}

		if(resourceURI.endsWith(".jpg") || resourceURI.endsWith(".jpeg") || resourceURI.endsWith("jpe")){
			return "image/jpeg";
		}else if(resourceURI.endsWith(".bmp")){
			return "image/x-ms-bmp";
		}else if(resourceURI.endsWith(".png")){
			return "image/png";
		}else if(resourceURI.endsWith(".gif")){
			return "image/gif";
		}else if(resourceURI.endsWith(".html") || resourceURI.endsWith(".htm")){
			return "text/html";
		}else if(resourceURI.endsWith(".txt") || resourceURI.endsWith(".rtx") || resourceURI.endsWith(".text")){
			return "text/plain";
		}else if(resourceURI.endsWith(".xml")){
			return "text/xml";
		}else if(resourceURI.endsWith(".css")){
			return "text/css";
		}else if(resourceURI.endsWith(".js")){
			return "text/javascript";
		}else if(resourceURI.endsWith("doc") || resourceURI.endsWith("docx")){
			return "application/msword";
		}else if(resourceURI.endsWith("pdf")){
			return "application/pdf";
		}else{
			return "application/octet-stream";
		}
		//this.contentType = contentTypeMap.get(resourceURI);
	}


	private void setHeaderFieldsAndCreateHeader(String statusCode, String htmlBody) {
		// TODO Auto-generated method stub
		

	}


	}
	public void createResponseHeader() {
		// TODO Auto-generated method stub
		
	}
	
}
