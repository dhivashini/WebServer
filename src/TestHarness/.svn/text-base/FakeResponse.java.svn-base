package edu.upenn.cis.cis555.webserver.TestHarness;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis555.webserver.HttpRequest;
import edu.upenn.cis.cis555.webserver.HttpResponse;
import edu.upenn.cis.cis555.webserver.HttpServer;
import edu.upenn.cis.cis555.webserver.HttpWorker;

/**
 * @author tjgreen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FakeResponse implements HttpServletResponse {

	private HttpWorker m_httpWorker;
	private HttpResponse m_httpResponse;
	private HashMap<String, ArrayList<String> > servletHeaderValueMap;
	private boolean obtainedWriter;
	private String m_charencoding;
	private String m_contentType;
	private String m_contentLength;
	private int m_buffersize;
	private boolean m_committed;
	private Locale m_locale;

	public static Logger logger = Logger.getLogger(FakeResponse.class);
	
	public FakeResponse(){
		
	}
	
	public FakeResponse(HttpWorker httpWorker){
		this.m_httpWorker = httpWorker;
		this.m_httpResponse = httpWorker.getHttpResponse();
		this.servletHeaderValueMap = this.m_httpResponse.getServletHeaderValueMap();
		obtainedWriter = false;
		m_charencoding = "ISO-8859-1";
		m_contentType  = "text/html" + "; charset=" + m_charencoding;
		m_buffersize   = 0; //keep it default
		m_committed    = false;
		m_locale	   = new Locale("en","US");
	}
	
	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie cookie) {
		String headerName 	= "Set-Cookie";
		String key			= headerName.toUpperCase();
		if(servletHeaderValueMap.containsKey(key)){
			ArrayList<String> valueList = servletHeaderValueMap.get(key);
			String cookieString = m_httpWorker.getHttpResponse().convertCookieToString(cookie);
			valueList.add(cookieString);
			servletHeaderValueMap.put(key, valueList);
		}
		else{
			ArrayList<String> valueList = new ArrayList<String>();
			String cookieString = m_httpWorker.getHttpResponse().convertCookieToString(cookie);
			valueList.add(cookieString);
			servletHeaderValueMap.put(key, valueList);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String headerName) {
		return servletHeaderValueMap.containsKey(headerName.toUpperCase());		
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	public String encodeURL(String arg0) {
		return arg0;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String arg0) {
		return arg0;
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	public String encodeUrl(String arg0) {
		return arg0;
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	public void sendError(int status, String message) throws IOException {
		if(isCommitted()){
			throw new IllegalStateException();
		}
		else{
			String statusString = Integer.toString(status);
			String statusCode = HttpServer.statusCodeMap.get(statusString);
			resetBuffer();
			m_httpWorker.getBaos().write(message.getBytes());
			this.m_httpWorker.getHttpResponse().setStatusCode(statusCode);
			m_committed = true;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int status) throws IOException {
		if(isCommitted()){
			throw new IllegalStateException();
		}
		else{
			String statusString = Integer.toString(status);
			String statusCode = HttpServer.statusCodeMap.get(statusString);
			resetBuffer();
			m_httpWorker.getBaos().write(statusCode.getBytes());
			this.m_httpWorker.getHttpResponse().setStatusCode(statusCode);
			m_committed = true;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String arg0) throws IOException {
		if(isCommitted()){
			throw new IllegalStateException();
		}
		else{
			m_httpResponse.setStatusCode("302 Redirect");
			System.out.println("[DEBUG] redirect to " + arg0 + " requested");
			System.out.println("[DEBUG] stack trace: ");
			Exception e = new Exception();
			StackTraceElement[] frames = e.getStackTrace();
			for (int i = 0; i < frames.length; i++) {
				System.out.print("[DEBUG]   ");
				System.out.println(frames[i].toString());
			}
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 */
	public void setDateHeader(String dateHeader, long time) {
		//Lesson - HashMap put replaces the content
		String dateString = m_httpResponse.getGMTDateTime(time);
		ArrayList<String> valueList = new ArrayList<String>();
		valueList.add(dateString);
		servletHeaderValueMap.put(dateHeader.toUpperCase(), valueList);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 */
	public void addDateHeader(String dateHeader, long time) {
		String dateString = m_httpResponse.getGMTDateTime(time);
		ArrayList<String> valueList = servletHeaderValueMap.get(dateHeader.toUpperCase());
		
		if(valueList == null){
			//No mapping exists
			valueList = new ArrayList<String>();
			valueList.add(dateString);
			servletHeaderValueMap.put(dateHeader.toUpperCase(), valueList);
		}
		else{
			//Mapping exists - So append it to the array list
			valueList.add(dateString);
			servletHeaderValueMap.put(dateHeader.toUpperCase(), valueList);
		}
	}
	
	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String headerName, String headerValue) {
		ArrayList<String> valueList;
		if(containsHeader(headerName)){
			// If the Header Value is already set, then remove from HashMap
			// Clear the list and add the new entry
			// Put it in the hash map again
			valueList = servletHeaderValueMap.remove(headerName.toUpperCase());
			valueList.clear();
			valueList.add(headerValue);
			servletHeaderValueMap.put(headerName.toUpperCase(), valueList);
		}
		else{
			//Header Value is not set already
			valueList = new ArrayList<String>();
			valueList.add(headerValue);
			servletHeaderValueMap.put(headerName.toUpperCase(), valueList);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String headerName, String headerValue) {
		ArrayList<String> valueList;
		if(containsHeader(headerName)){
			valueList = servletHeaderValueMap.get(headerName.toUpperCase());
			valueList.add(headerValue);
			servletHeaderValueMap.put(headerName.toUpperCase(), valueList);
		}
		else{
			valueList = new ArrayList<String>();
			valueList.add(headerValue);
			servletHeaderValueMap.put(headerName.toUpperCase(), valueList);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 */
	public void setIntHeader(String headerName, int headerIntValue) {
		String headerValue = Integer.toString(headerIntValue);
		setHeader(headerName, headerValue);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 */
	public void addIntHeader(String headerName, int headerIntValue) {
		String headerValue = Integer.toString(headerIntValue);
		addHeader(headerName, headerValue);
	}

	/* (non-Javadoc) COMPLETED - But more codes to be added to map
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int status) {
		resetBuffer();
		String statusCode = Integer.toString(status);
		this.m_httpResponse.setStatusCode(HttpServer.statusCodeMap.get(statusCode));
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return m_charencoding;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		return m_contentType;
	}

	/* (non-Javadoc) NOT REQUIRED
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	public PrintWriter getWriter() throws IOException {
		if(obtainedWriter == true){
			//If Writer has already been called return null
			return null;
		}else{
			//First time writer is called. Used for setContentType, buffer size etc
			obtainedWriter = true;
		}
		
		if(m_buffersize == 0){
			//user has not set anything - so create default size 32
			this.m_httpWorker.setBaos(new ByteArrayOutputStream()); 
		}
		else{
			//buffersize has some good value - use it
			this.m_httpWorker.setBaos(new ByteArrayOutputStream(m_buffersize)); 
		}
		
		//One more additional check
		if(this.getHttpWorker().getBaos() == null)
			this.m_httpWorker.setBaos(new ByteArrayOutputStream());
		
		return new PrintWriter(this.getHttpWorker().getBaos(),true);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String charencoding) {
		//Don't do anything since we should just have default value of ISO-8859-1
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int contentLength) {
		m_contentLength = Integer.toString(contentLength);
		
		//Put it in our servlet response header map to be accessed during response sending
		String headerName 			= "Content-Length";
		ArrayList<String> valueList = new ArrayList<String>();
		valueList.add(m_contentLength);
		servletHeaderValueMap.put(headerName.toUpperCase(), valueList);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String contentType) {
		if(obtainedWriter || isCommitted()){
			//If getWriter has already been invoked don't do anything.
			return;
		}
		else{
			//Set it locally to be accessed by get ContentType
			m_contentType		= contentType + "; charset=" + m_charencoding;
			
			//Put it in our servlet response header map to be accessed during response sending
			String headerName 	= "Content-Type";
			String key			= headerName.toUpperCase();
			ArrayList<String> valueList = new ArrayList<String>();
			valueList.add(contentType);
			servletHeaderValueMap.put(key, valueList);
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	public void setBufferSize(int buffersize) {
		//Set the local variable here
		if(obtainedWriter || isCommitted()){
			//Writer has already been obtained or Response Committed. So throw an IllegalStateException
			throw new IllegalStateException();
		}
		else{
			//Writer has not been obtained. So, set the buffer size and create in response Writer
			m_buffersize = buffersize;
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#getBufferSize()
	 */
	public int getBufferSize() {
		if(m_buffersize == 0)
			return 32; //default size
		return m_buffersize;
	}

	/* (non-Javadoc) COMPLETED - VERIFY
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	public void flushBuffer() throws IOException {
		m_committed = true;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	public void resetBuffer() {
		if(isCommitted()){
			throw new IllegalStateException();
		}
		else{
			this.m_httpWorker.getBaos().reset();
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	public boolean isCommitted() {
		return m_committed;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset() {
		if(isCommitted()){
			throw new IllegalStateException();
		}
		else{
			m_httpWorker.getHttpResponse().setStatusCode(""); //Clear status code
			servletHeaderValueMap.clear(); //Clear Headers
			resetBuffer(); //Clear Buffer
		}
	}

	/* (non-Javadoc) VERIFY
	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		m_locale = locale;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	public Locale getLocale() {
		return m_locale;
	}

	//User Defined Methods
	public HttpWorker getHttpWorker() {
		return m_httpWorker;
	}

	public void setM_httpWorker(HttpWorker mHttpWorker) {
		m_httpWorker = mHttpWorker;
	}

	public HashMap<String, ArrayList<String>> getServletHeaderValueMap() {
		return servletHeaderValueMap;
	}

	public void setServletHeaderValueMap(
			HashMap<String, ArrayList<String>> servletHeaderValueMap) {
		this.servletHeaderValueMap = servletHeaderValueMap;
	}
}
