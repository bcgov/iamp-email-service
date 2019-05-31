package ca.bc.gov.iamp.email;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import ca.bc.gov.iamp.email.rules.SmtpServerRule;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-testing.properties")
public class ApplicationTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Before
    public void init() {
    	Awaitility.setDefaultPollInterval(100, TimeUnit.MILLISECONDS);
    	Awaitility.setDefaultPollDelay(Duration.ONE_SECOND);
    	Awaitility.setDefaultTimeout(Duration.TEN_SECONDS);
    }
    
	@Test
	public void testSendEmailSuccess() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("subject", "subject");
		map.add("to", "to@to.com");
		map.add("cc", "cc@cc.com");
		map.add("bcc", "bcc@bcc.com");
		map.add("body", "body");
		//map.add("attachments", new MockMultipartFile("data", "filename.txt", "text/plain", "awesome email attachment".getBytes()).getResource().getFile());

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/emails"), HttpMethod.POST, entity,
				String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Awaitility.await().until(receivedMessagesGreaterThan(0));
	}
	
	private Callable<Boolean> receivedMessagesGreaterThan(int num) {
	      return new Callable<Boolean>() {
	            public Boolean call() throws Exception {
	        		MimeMessage[] receivedMessages = smtpServerRule.getMessages();
	                return num < receivedMessages.length;
	            }
	      };
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
