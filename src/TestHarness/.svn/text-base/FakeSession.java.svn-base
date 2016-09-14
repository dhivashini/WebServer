package edu.upenn.cis.cis555.webserver.TestHarness;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;

import edu.upenn.cis.cis555.webserver.HttpServer;
import edu.upenn.cis.cis555.webserver.HttpWorker;

/**
 * @author Todd J. Green
 */
public class FakeSession implements HttpSession {
	
	private Properties m_props = new Properties();
	private boolean m_valid = true;
	private String m_sessionId;
	private HttpWorker httpWorker;
	private long m_creationTime;
	private long m_lastAccessed;

	public static Logger logger = Logger.getLogger(FakeSession.class);
	
	public FakeSession(HttpWorker httpWorker){
		m_sessionId 	= UUID.randomUUID().toString();
		this.httpWorker = httpWorker;
		m_creationTime	= System.currentTimeMillis();
		m_lastAccessed	= 0; //First time this is being accessed when created
	}
	
	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public long getCreationTime() {
		if(isValid()){
			return m_creationTime;
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		return m_sessionId;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		if(isValid()){
			return m_lastAccessed;
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		return this.httpWorker.getHttpServer().getServletContext();
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if(isValid()){
			return m_props.get(name);
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		if(isValid()){
			return m_props.keys();
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc) COMPLETED - VERIFY about value being null
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		if(isValid()){
			if(value == null){
				//As per specs, if value is null, it works like remove attribute.
				removeAttribute(name);
			}
			else{
				m_props.put(name, value);
			}
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		if(isValid()){
			m_props.remove(name);	
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) DEPRECATED
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc) COMPLETED
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		if(isValid()){
			m_valid = false;
			//We invalidated. So, remove from the sessionMap
			this.httpWorker.getHttpServer().getSessionMap().remove(this.getId());
		}
		else{
			//Called on an already invalidated session
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc) VERIFY
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		if(isValid()){
			//Do Something - Ask TA
			return false;
		}
		else{
			throw new IllegalStateException();
		}
	}

	/* COMPLETED */
	public boolean isValid() {
		return m_valid;
	}
	
	//USER DEFINED FUNCTIONS
	public void setLastAccessed(long mLastAccessed) {
		m_lastAccessed = mLastAccessed;
	}
}
