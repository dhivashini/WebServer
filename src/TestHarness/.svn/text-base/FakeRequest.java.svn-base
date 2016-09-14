package edu.upenn.cis.cis555.webserver.TestHarness;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.upenn.cis.cis555.webserver.HttpRequest;
import edu.upenn.cis.cis555.webserver.HttpServer;
import edu.upenn.cis.cis555.webserver.HttpWorker;

/**
 * @author Todd J. Green
 */
public class FakeRequest implements HttpServletRequest{

	private HttpWorker m_httpWorker;
	private HttpRequest m_httpRequest;
	private HashMap<String,ArrayList<String> > headerNameValuePair;

	private Properties m_params 	= new Properties();
	private Properties m_props 		= new Properties();
	private FakeSession m_session 	= null;
	private String m_method			= null;
	private String m_charencoding 	= "ISO-8859-1";
	private Locale m_locale			= null;
	private String m_authType		= null;
	private boolean obtainedReader	= false;
	
	public FakeRequest(HttpRequest httpRequest) {
		this.m_httpRequest = httpRequest;
	}
	
	public FakeRequest(FakeSession session, HttpWorker httpWorker) {
		m_session    		= session;
		m_httpWorker 		= httpWorker;
		m_httpRequest		= m_httpWorker.getHttpRequest();
		headerNameValuePair	= m_httpRequest.getRequestHeaderValueMap();
		m_method			= m_httpRequest.getHttpMethod();
		m_authType			= "BASIC";
	}
	
	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		return m_authType;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		ArrayList<Cookie> cookieList = new ArrayList<Cookie>();
		
		//Get the Cookie ArrayList from the the Request Header
		ArrayList<String> cookies = headerNameValuePair.get("COOKIE");
		if(cookies == null){
			//No Cookies
			return null;
		}
		else{
			Iterator<String> itr = cookies.iterator();
			while(itr.hasNext()){
				//cookie variable below can be one cookie or list of cookies
				String cookie = itr.next();
				
				//split the line containing cookies
				String[] cookieSplitted = cookie.split(";");
				
				//Iterate through the array of cookies and extract name and value
				for(int i=0; i<cookieSplitted.length; i++){
					String namevalue = cookieSplitted[i].trim();
					String[] namevalueSplitted = namevalue.split("=");
					
					if(namevalueSplitted.length < 2){
						//Error checking - if cookie is not of the form name = value
						continue;
					}
					
					String name  = namevalueSplitted[0];
					String value = namevalueSplitted[1];
					
					cookieList.add(new Cookie(name, value));
				}
			}
		}
		
		Cookie[] c = new Cookie[cookieList.size()];
		for(int i=0; i<cookieList.size(); i++){
			c[i] = cookieList.get(i);
		}
		return c;
		
		//Is the following way of type casting better?
		//return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String headerName) {
		long timeInMillis = 0;
		boolean containsDateHeader  = headerNameValuePair.containsKey(headerName.toUpperCase());
		if(containsDateHeader){
			//If there is a header by this name.
			String dateString 	= headerNameValuePair.get(headerName.toUpperCase()).get(0);
			dateString		  	= dateString.trim();
			timeInMillis 		= m_httpRequest.getDateTimeinMillis(dateString);
			if(timeInMillis == 0){
				//Means we got a parse exception from getDateTimeinMillis function which means
				//this is not a valid Date String. So, throw IllegalArgumentException
				throw new IllegalArgumentException();
			}
			else{
				//We got a finite value for date and so return it to user.
				return timeInMillis;
			}
		}
		else{
			//When there is no such header by the name passed as argument
			return -1;
		}		
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String headerName) {
		if(headerNameValuePair.containsKey(headerName.toUpperCase())){
			//if we find the header, return the header value in String
			return headerNameValuePair.get(headerName.toUpperCase()).get(0).trim();
		}
		else{
			//implies the specified header name was not found in the request
			return null;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration<String> getHeaders(String headerName) {
		if(headerNameValuePair.containsKey(headerName.toUpperCase())){
			ArrayList<String> valueList = headerNameValuePair.get(headerName.toUpperCase());
			return Collections.enumeration(valueList);
		}
		else{
			//Key is not there - So return an empty enumeration
			ArrayList<String> valueList = new ArrayList<String>();
			return Collections.enumeration(valueList);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration<String> getHeaderNames() {
		if(headerNameValuePair.keySet().isEmpty()){
			//If key set is empty, create an empty hash map and send it.
			HashMap<String, ArrayList<String> > emptyHashMap = new HashMap<String,ArrayList<String> >();
			return Collections.enumeration(emptyHashMap.keySet());
		}
		else{
			//keySet is not empty - so convert it to enumeration and send it over
			return Collections.enumeration(headerNameValuePair.keySet());

		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String headerName) {
		if(headerNameValuePair.containsKey(headerName.toUpperCase())){
			//if we find the header, return the header value in String
			try{
				return Integer.parseInt(headerNameValuePair.get(headerName.toUpperCase()).get(0));
			}catch(NumberFormatException nfe){
				throw new NumberFormatException();
			}
		}
		else{
			//implies the specified header name was not found in the request
			return -1;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return m_method;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		String requestURI = m_httpRequest.getResourceURI();
		String urlPattern = m_httpRequest.getUrlPattern();
		if(urlPattern.contains("*")){
			//If it contains *, then find what * is and return
			int indexOfStar = urlPattern.indexOf("*"); //Indices in URL Pattern and Request URI overlap
			return requestURI.substring(indexOfStar - 1);
		}
		else{
			//If URL Pattern doesn't contain a *, then it is an exact match. So, return null
			return null;
		}
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		// Have to change if we are implementing extra credit 6.4
		//Refer Piazza @164
		return "";
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		return m_httpRequest.getQueryString();
	}

	/* (non-Javadoc) VERIFY
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		String sessionId = m_httpRequest.getSessionId(m_httpWorker);
		if(sessionId.equals("")){
			sessionId = null;
		}
		return sessionId;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		int indexOfQuestionMark = m_httpRequest.getResourceURI().indexOf('?');
		if(indexOfQuestionMark == -1){
			//Query String Not Found
			return m_httpRequest.getResourceURI();
		}
		else{
			//Query String found
			return m_httpRequest.getResourceURI().substring(1,indexOfQuestionMark);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		String protocol 		= "http";
		String hostname			= m_httpWorker.getHttpServer().getHostName();
		String serverPath		= this.getRequestURI();
		StringBuffer requestURL = new StringBuffer(protocol + "://" + hostname + serverPath); //No Query String Params
		return requestURL;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		String requestURI = m_httpRequest.getResourceURI();
		String urlPattern = m_httpRequest.getUrlPattern();
		if(urlPattern.equals("/*")){
			//Given in the specs
			return "";
		}
		
		if(urlPattern.contains("*")){
			//If it contains *, then return the part which is prior to /*
			int indexOfStar = urlPattern.indexOf("*");
			return requestURI.substring(0,indexOfStar-1);
		}
		else{
			//It does not contain a * => exact match => return full string
			return requestURI;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean toCreateSession) {
		if (toCreateSession) {
			if (! hasSession()) {
				m_session = new FakeSession(this.m_httpWorker);
				this.m_httpWorker.getHttpServer().getSessionMap().put(m_session.getId(), m_session);
				
				String key = "SET-COOKIE";
				ArrayList<String> valueList = new ArrayList<String>();
				valueList.add("SessionID=" + m_session.getId());
				this.m_httpWorker.getHttpResponse().getServletHeaderValueMap().put(key, valueList);
			}
			else{
				//There exists a valid session - So return it
				return this.m_httpWorker.getHttpServer().getSessionMap().get(m_session.getId());
			}
		} else {
			if (! hasSession()) {
				m_session = null;
			}
		}
		return m_session;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		return getSession(true);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		String sessionId = m_httpRequest.getSessionId(m_httpWorker);
		if(!sessionId.equals("")){
			//If client specifies a session id
			FakeSession session = (FakeSession)this.m_httpWorker.getHttpServer().getSessionMap().get(sessionId);
			if(session != null){
				//Session Available - Check if valid or not
				return session.isValid();
			}
		}
		return false;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		if(m_charencoding == null)
			return "ISO-8859-1"; //Default Encoding
		else
			return m_charencoding;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String charencoding)
			throws UnsupportedEncodingException {
		if(!obtainedReader)
			m_charencoding = charencoding;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		if(headerNameValuePair.containsKey("CONTENT-LENGTH")){
			return Integer.parseInt(headerNameValuePair.get("CONTENT-LENGTH").get(0));
		}
		else{
			return -1;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		if(headerNameValuePair.containsKey("CONTENT-TYPE")){
			String contentType = headerNameValuePair.get("CONTENT-TYPE").get(0);
			String[] contentTypeSplitted = contentType.split(";");
			return contentTypeSplitted[0].trim();
		}
		else{
			return null;
		}
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		obtainedReader = true;
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String name) {
		//return m_params.getProperty(arg0);
		ArrayList<String> valueList = this.m_httpRequest.getParameterMap().get(name);
		if(valueList != null){
			return valueList.get(0);
		}
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		//return m_params.keys();
		return Collections.enumeration(m_httpRequest.getParameterMap().keySet());
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String name) {
		ArrayList<String> valueList = m_httpRequest.getParameterMap().get(name);
		if(valueList != null){
			//Parameter exists
			String[] values = new String[valueList.size()];
			for(int i=0; i<valueList.size(); i++){
				values[i] = valueList.get(i);
			}
			return values;
		}
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap(){
		HashMap<String, String[]> dest 			= new HashMap<String, String[]>();
		HashMap<String, ArrayList<String> > src = m_httpRequest.getParameterMap(); 
		
		String key;
		ArrayList<String> valueList = null;
		String[] valueArray;
		
		//Iterate through each row and then convert array list to array and store
		Map.Entry<String, ArrayList<String> > me;
		Iterator<Map.Entry<String, ArrayList<String> > > iterator = src.entrySet().iterator();		
		while(iterator.hasNext()){
			me 			= iterator.next();
			key 		= me.getKey();
			valueList 	= me.getValue();
			valueArray 	= new String[valueList.size()];
			for(int i=0; i<valueList.size(); i++){
				valueArray[i] = valueList.get(i);
			}
			dest.put(key, valueArray);
		}
		
		return dest;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		return m_httpRequest.getHttpVersion();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		return "http"; //HW1 document asks us to hard code this
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		if(headerNameValuePair.containsKey("HOST")){
			String hostname 			= headerNameValuePair.get("HOST").get(0);
			String[] hostnameSplitted 	= hostname.split(":");
			String servername			= hostnameSplitted[0].trim();
			return servername;
		}
		else{
			return "localhost";
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		if(headerNameValuePair.containsKey("HOST")){
			String hostname 			= headerNameValuePair.get("HOST").get(0);
			String[] hostnameSplitted 	= hostname.split(":");
			int serverport				= Integer.parseInt(hostnameSplitted[1].trim());
			return serverport;
		}
		else{
			return m_httpWorker.getHttpServer().getServerPort();
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		if(!obtainedReader){
			obtainedReader 				= true;
			String requestBody 			= m_httpRequest.getRequestBody();
			ByteArrayInputStream bais 	= new ByteArrayInputStream(requestBody.getBytes()); 			
			InputStreamReader isr		= null;
			try{
				isr = new InputStreamReader(bais, m_charencoding);
			}
			catch(UnsupportedEncodingException uee){
				throw uee;
			}
			return new BufferedReader(isr);
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		return m_httpWorker.getClientSocket().getInetAddress().getHostAddress();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		return m_httpWorker.getClientSocket().getInetAddress().getCanonicalHostName();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		m_props.remove(arg0);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		return m_locale;
	}
	
	//COMPLETED
	public void setLocale(Locale locale){
		m_locale = locale;
	}
	
	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		return false;
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)COMPLETED
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
		return m_httpWorker.getClientSocket().getPort();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		return m_httpWorker.getClientSocket().getLocalAddress().getHostName();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		return m_httpWorker.getClientSocket().getLocalAddress().getHostAddress();
	}

	/* (non-Javadoc)COMPLETED
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		return m_httpWorker.getClientSocket().getLocalPort(); 
	}

	/* COMPLETED */
	public void setMethod(String method) {
		m_method = method;
	}
	
	/* COMPLETED */
	public void setParameter(String key, String value) {
		//m_params.setProperty(key, value);
		ArrayList<String> valueList = m_httpRequest.getParameterMap().get(key);
		if(valueList == null){
			valueList = new ArrayList<String>();
		}
		valueList.add(value);
		m_httpRequest.getParameterMap().put(key, valueList);
	}
	
	/* COMPLETED */
	public void clearParameters() {
		//m_params.clear();
		m_httpRequest.getParameterMap().clear();
	}
	
	/* COMPLETED */
	public boolean hasSession() {
		return ((m_session != null) && m_session.isValid());
	}
		
}
