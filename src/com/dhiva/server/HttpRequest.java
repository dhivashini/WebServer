package com.dhiva.server;

public class HttpRequest {
	private String resourceURI;
	private String httpVersion;

	public enum HttpMethod {
		GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), BAD("BAD"), HEAD("HEAD");

		private String httpMethod;

		HttpMethod(String requestType) {
			this.httpMethod = requestType;
		}

		public String getHttpMethod() {
			return httpMethod.toString();
		}

		public HttpMethod setHttpMethod(String text) {
			if (text != null) {
				for (HttpMethod b : HttpMethod.values()) {
					if (text.equalsIgnoreCase(b.httpMethod)) {
						return b;
					}
				}
			}
			return null;
		}
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
