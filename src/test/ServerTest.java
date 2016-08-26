package test;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import com.dhiva.server.HttpParse;

public class ServerTest  {

	@Test
	public void test() {
		StringBuffer request = new StringBuffer()
		           .append("GET /P3.pdf HTTP/1.1\n")
		           .append("Host: 127.0.0.1:4000\n")
		           .append("Connection: keep-alive\n")
		           .append("Cache-Control: max-age=0\n")
		           .append("Upgrade-Insecure-Requests: 1\n")
		           .append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36\n")
		           .append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n ")
		           .append("Accept-Encoding: gzip, deflate, sdch\n")
		           .append(")Accept-Language: en-US,en;q=0.8\n");
		HttpParse myparser = new HttpParse(request);
		myparser.parse();
	}

}


