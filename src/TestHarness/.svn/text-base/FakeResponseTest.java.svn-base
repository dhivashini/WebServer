package edu.upenn.cis.cis555.webserver.TestHarness;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import edu.upenn.cis.cis555.webserver.*;
import junit.framework.TestCase;

public class FakeResponseTest extends TestCase {
	
	HttpWorker httpWorker;
	HttpResponse httpResponse;
	
	public void setUp(){
		httpWorker = new HttpWorker();
		httpResponse = new HttpResponse();
		httpWorker.setHttpResponse(httpResponse);
		httpWorker.setBaos(new ByteArrayOutputStream());
	}
	
//	public void testFakeResponse() {
//		fail("Not yet implemented");
//	}
	
	//Adding a cookie test
	public void testAddCookie() {
		FakeResponse fr = new FakeResponse();
		HashMap<String, ArrayList<String> > servletHeaderValueMap = new HashMap<String, ArrayList<String> >();
		fr.setServletHeaderValueMap(servletHeaderValueMap);
		fr.setM_httpWorker(httpWorker);
		Cookie c = new Cookie("name3", "value3");
		fr.addCookie(c);
		c = new Cookie("name1","value1");
		fr.addCookie(c);
		ArrayList<String> valueList = fr.getServletHeaderValueMap().get("SET-COOKIE");
		assertEquals(valueList.get(0),"name3=value3; ");
	}

//	public void testContainsHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testEncodeURL() {
//		fail("Not yet implemented");
//	}
//
//	public void testEncodeRedirectURL() {
//		fail("Not yet implemented");
//	}
//
//	public void testEncodeUrl() {
//		fail("Not yet implemented");
//	}
//
//	public void testEncodeRedirectUrl() {
//		fail("Not yet implemented");
//	}
//
//	public void testSendErrorIntString() {
//		fail("Not yet implemented");
//	}
//
//	public void testSendErrorInt() {
//		fail("Not yet implemented");
//	}
//
//	public void testSendRedirect() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetDateHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testAddDateHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testAddHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetIntHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testAddIntHeader() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetStatusInt() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetStatusIntString() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetCharacterEncoding() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetContentType() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetOutputStream() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetWriter() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetCharacterEncoding() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetContentLength() {
//		fail("Not yet implemented");
//	}

	//Tested for set Content Type - Check for IllegalStateException
	public void testSetContentType() {
		FakeResponse fr = new FakeResponse();
		fr.setM_httpWorker(httpWorker);
		try {
			PrintWriter out = fr.getWriter();
			fr.setContentType("image/jpg");
		} catch (IllegalStateException e) {
			assert(true);
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
//
//	public void testSetBufferSize() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetBufferSize() {
//		fail("Not yet implemented");
//	}
//
////	public void testFlushBuffer() {
//		fail("Not yet implemented");
//	}

	//Buffer Reset Test
	public void testResetBuffer() {
		FakeResponse fr = new FakeResponse();
		fr.setM_httpWorker(httpWorker);
		fr.resetBuffer();
		assertEquals(httpWorker.getBaos().toString(),"");
	}
//
//	public void testIsCommitted() {
//		fail("Not yet implemented");
//	}
//
//	public void testReset() {
//		fail("Not yet implemented");
//	}
//
//	public void testSetLocale() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetLocale() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetHttpWorker() {
//		fail("Not yet implemented");
//	}
}
