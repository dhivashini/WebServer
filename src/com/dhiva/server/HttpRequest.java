package com.dhiva.server;

public class HttpRequest {
	private String resourceURI;
	private String httpVersion;
	private HttpMethod httpMethod;

	public enum HttpMethod {
		GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), BAD("BAD"), HEAD("HEAD");

		private String httpMethod;

		HttpMethod(String requestType) {
			this.httpMethod = requestType;
		}
	}

	public void setHttpMethod(String requestType) {
		if (requestType != null) {
			for (HttpMethod b : HttpMethod.values()) {
				if (requestType.equalsIgnoreCase(b.httpMethod)) {
					this.httpMethod = b;

				}
			}
		}
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpVersion(String requestVersion) {
		this.httpVersion = requestVersion;
	}

	public void setResourceURI(String requestFile) {
		this.resourceURI = requestFile;
	}

	public String getResourceURI() {
		return resourceURI;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

}
