/**
 * 
 */
package com.zenika;

import com.zenika.domain.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author acogoluegnes
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

	@Value("${local.server.port}") int port;

	TestRestTemplate tpl = new TestRestTemplate();

	@Ignore
	// TODO 18 Enlever @Ignore et lancer le test
	// Le test doit passer
	// Ce test lance véritablement l'application web
    // et effectue de véritables requetes HTTP
	@Test public void listUsers() {
		User[] users = tpl.getForObject(baseUrl() + "users", User[].class);
		Assert.assertEquals(2,users.length);
	}

	String baseUrl() {
		return "http://localhost:"+port+"/";
	}

}
