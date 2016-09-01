package com.dhiva.server;

public class HttpRequest {

	enum HttpMethod {
		GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT, BAD
	};

	private HttpMethod httpMethod;
	private String resourceURI;
	private String httpVersion;

	public void setHttpVersion(String requestVersion) {
		this.httpVersion = requestVersion;
	}

	public void setResourceURI(String requestFile) {
		this.resourceURI = requestFile;
	}

	public void setHttpMethod(String requestType) {
		String httpMethod = requestType;
		if (httpMethod.equals("GET")) {
			this.httpMethod = HttpMethod.GET;
		} else if (httpMethod.equals("HEAD")) {
			this.httpMethod = HttpMethod.HEAD;
		} else if (httpMethod.equals("POST")) {
			this.httpMethod = HttpMethod.POST;
		} else if (httpMethod.equals("PUT")) {
			this.httpMethod = HttpMethod.PUT;
		} else if (httpMethod.equals("DELETE")) {
			this.httpMethod = HttpMethod.DELETE;
		} else if (httpMethod.equals("TRACE")) {
			this.httpMethod = HttpMethod.TRACE;
		} else if (httpMethod.equals("CONNECT")) {
			this.httpMethod = HttpMethod.CONNECT;
		} else {
			this.httpMethod = HttpMethod.BAD;
		}
	}
	
	public String getHttpMethod() {
		return httpMethod.toString();
	}

	public String getResourceURI() {
		return resourceURI;
	}

	public String getHttpVersion() {
		return httpVersion;
	}


}
