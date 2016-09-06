package com.dhiva.server;

public class HttpRequestParser {
	private StringBuffer clientRequest;
	//HttpMethod methodObj ;

	public HttpRequestParser(StringBuffer clientRequest) {
		this.clientRequest = clientRequest;
	}

	public HttpRequest parse() {
		String request = clientRequest.toString();
		String[] splited = request.split("\\s+");
		HttpRequest requestObj = new HttpRequest();
		requestObj.setHttpMethod(splited[0]);
		requestObj.setResourceURI(splited[1]);
		requestObj.setHttpVersion(splited[2]);
		return requestObj;
	}
	/*public HttpMethod parseMethod(){
		String request = clientRequest.toString();
		String[] splited = request.split("\\s+");
		methodObj.setHttpMethod("GET");
		return methodObj;
	}*/

}
