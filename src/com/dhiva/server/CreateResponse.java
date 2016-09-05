package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.dhiva.server.HttpRequest.HttpMethod;

public class CreateResponse {
	private HttpRequest requestObj;
	private HttpMethod methodObj;
	private String rootDirectory;
	private HttpResponse responseObj;

	public CreateResponse(HttpRequest requestObj, HttpMethod methodObj) {
		this.requestObj = requestObj;
		this.methodObj = methodObj;
	}

	public void createResponseBody() {
		// Check for HTTP Version
		String httpVersion = requestObj.getHttpVersion();
		String httpMethod = methodObj.getHttpMethod();
		String resourceURI = requestObj.getResourceURI();
		ClientProcessingRunnable clientObj = new ClientProcessingRunnable();
		this.rootDirectory = clientObj.getRootDirectory();
		responseObj.setHttpVersion(httpVersion);

		if (!httpVersion.equals("HTTP/1.1") && !httpVersion.equals("HTTP/1.0")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody);
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(String.valueOf(htmlBody.length()));
			return;
		}

		// Check for bad method name
		if (httpMethod.equals("BAD")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody);
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(String.valueOf(htmlBody.length()));
			return;
		}

		// Methods not implemented - send 501
		if (!httpMethod.equals("GET") && !httpMethod.equals("HEAD") && !httpMethod.equals("POST")) {
			String statusCode = "501 Not Implemented";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody);
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(String.valueOf(htmlBody.length()));
			return;
		}

		// Check for illegal access
		if (resourceURI.contains("..")) {
			String statusCode = "403 Access Forbidden";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody);
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(String.valueOf(htmlBody.length()));
			return;
		}

		if (!resourceURI.startsWith("/")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody);
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(String.valueOf(htmlBody.length()));
			return;
		}

		if (httpMethod.equals("GET")) {
			final String FILE_TO_SEND = rootDirectory + resourceURI;
			File myFile = new File(FILE_TO_SEND);
			try {
				if (!myFile.exists()) {
					String statusCode = "404 Not Found";
					String htmlBody = "<html><body>" + statusCode + "</body></html>";
					responseObj.setStatusCode(statusCode);
					responseObj.setResponseBody(htmlBody);
					responseObj.setContentType("text/html");
					getGMTDateTime();
					responseObj.setContentLength(String.valueOf(htmlBody.length()));
				} else if (myFile.exists() && myFile.isFile()) {
					byte[] mybytearray = new byte[(int) myFile.length()];
					String statusCode = "200 OK";
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
					bis.read(mybytearray, 0, mybytearray.length);
					bis.close();
					responseObj.setStatusCode(statusCode);
					setFileType();
					getGMTDateTime();
					responseObj.setContentLength(String.valueOf(mybytearray.length));
					responseObj.setResponseBody(mybytearray.toString());
				} else if (myFile.exists() && myFile.isDirectory()) {
					File[] listOfFiles = myFile.listFiles();
					List<String> results = new ArrayList<String>();
					for (File file : listOfFiles) {
						if (file.isFile()) {
							results.add(file.getName());
						}
					}
					for (String fileName : results) {
						String statusCode = "200 OK";
						String startTag = "<!DOCTYPE html> <html> <body><a href=\"";
						String endTag = "</a></body> </html>";
						String displayName = "\">" + fileName;
						String link = startTag + myFile.getName() + "/" + fileName + displayName + endTag;
						responseObj.setStatusCode(statusCode);
						responseObj.setContentLength(String.valueOf(link.length()));
						responseObj.setResponseBody(link);
						responseObj.setContentType("text/html");
						getGMTDateTime();
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if (httpMethod.equals("HEAD")) {
			final String FILE_TO_SEND = rootDirectory + resourceURI;
			File myFile = new File(FILE_TO_SEND);
			if (!myFile.exists()) {
				String statusCode = "404 Not Found";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				responseObj.setStatusCode(statusCode);
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(String.valueOf(htmlBody.length()));
				responseObj.setResponseBody(htmlBody);
			} else if (myFile.exists()) {
				String statusCode = "204 No Content";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				responseObj.setStatusCode(statusCode);
				responseObj.setResponseBody(htmlBody);
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(String.valueOf(htmlBody.length()));
			}
		}
	}

	public void getGMTDateTime() {
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		// Give it to me in GMT time.
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		responseObj.setDate(sdf.format(currentTime));
	}

	private void setFileType() {
		String resourceURI = requestObj.getResourceURI();
		if (resourceURI.endsWith(".jpg") || resourceURI.endsWith(".jpeg") || resourceURI.endsWith("jpe")) {
			responseObj.setContentType("image/jpeg");
		} else if (resourceURI.endsWith(".bmp")) {
			responseObj.setContentType("image/x-ms-bmp");
		} else if (resourceURI.endsWith(".png")) {
			responseObj.setContentType("image/png");
		} else if (resourceURI.endsWith(".gif")) {
			responseObj.setContentType("image/gif");
		} else if (resourceURI.endsWith(".html") || resourceURI.endsWith(".htm")) {
			responseObj.setContentType("text/html");
		} else if (resourceURI.endsWith(".txt") || resourceURI.endsWith(".rtx") || resourceURI.endsWith(".text")) {
			responseObj.setContentType("text/plain");
		} else if (resourceURI.endsWith(".xml")) {
			responseObj.setContentType("text/xml");
		} else if (resourceURI.endsWith(".css")) {
			responseObj.setContentType("text/css");
		} else if (resourceURI.endsWith(".js")) {
			responseObj.setContentType("text/javascript");
		} else if (resourceURI.endsWith("doc") || resourceURI.endsWith("docx")) {
			responseObj.setContentType("application/msword");
		} else if (resourceURI.endsWith("pdf")) {
			responseObj.setContentType("application/pdf");
		} else {
			responseObj.setContentType("application/octet-stream");
		}
	}
}
