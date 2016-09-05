package com.dhiva.server;

public class HttpResponse {
	private String responseHeader;
	private String responseBody; 
	private String statusCode;
	private String contentLength;
	private String contentType;
	private String httpVersion;

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

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getResponseHeader() {
		responseHeader = httpVersion + statusCode + contentType + contentLength;
		return responseHeader;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getResponseBody() {
		return responseBody;
	}
}
