package com.dhiva.server;

public class HttpResponse {
	private String responseHeader;
	private byte[] responseBody; 
	private String statusCode;
	private String contentLength;
	private String contentType;
	private String httpVersion;
	private String date;

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}
	public void setDate(String date) {
		this.date = "Date: "+date;
	}

	public String getDate() {
		return date;
	}

	public void setContentType(String contentType) {
		this.contentType = "Content-Type: "+contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = "Content-Length: "+contentLength;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getResponseHeader() {
		responseHeader = httpVersion +" "+ statusCode+"\r\n" + date + "\r\n"+contentType +"\r\n"+ contentLength+"\r\n\r\n";
		return responseHeader;
	}

	public void setResponseBody(byte[] mybytearray) {
		this.responseBody = mybytearray;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}
}
