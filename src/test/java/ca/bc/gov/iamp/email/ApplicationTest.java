/**
 * Copyright 2019 Province of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.bc.gov.iamp.email;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@Value("classpath:/sample/test-attachment.file")
	private Resource attachment;

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Rule
	public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

	@Before
	public void init() {
		Awaitility.setDefaultPollInterval(100, TimeUnit.MILLISECONDS);
		Awaitility.setDefaultPollDelay(Duration.ONE_SECOND);
		Awaitility.setDefaultTimeout(Duration.ONE_MINUTE);
	}

	@Test
	public void testSendEmailSuccess() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("subject", "subject");
		map.add("to", "to@to.com");
		map.add("to", "to2@to2.com");
		map.add("cc", "cc@cc.com");
		map.add("bcc", "bcc@bcc.com");
		map.add("body", "body");
		FileSystemResource a = new FileSystemResource(attachment.getFile());
		map.add("attachments", a);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/emails"), HttpMethod.POST,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		Awaitility.await().until(receivedMessagesGreaterThan(0));

		MimeMessage[] receivedMessages = smtpServerRule.getMessages();
		MimeMessage message = receivedMessages[0];
		assertEquals("subject", message.getSubject());
		assertEquals("to@to.com", message.getRecipients(Message.RecipientType.TO)[0].toString());
		assertEquals("cc@cc.com", message.getRecipients(Message.RecipientType.CC)[0].toString());
		// assertEquals("bcc@bcc.com",
		// message.getRecipients(Message.RecipientType.BCC)[0].toString());
		handleMessage(message);

	}

	public void handleMessage(Message message) throws IOException, MessagingException {
		Object content = message.getContent();
		if (content instanceof String) {
			assertEquals("body", content);
		} else if (content instanceof Multipart) {
			Multipart mp = (Multipart) content;
			handleMultipart(mp);
		}
	}

	public void handleMultipart(Multipart mp) throws IOException, MessagingException {
		int count = mp.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bp = mp.getBodyPart(i);
			Object content = bp.getContent();
			if (content instanceof String) {
				assertEquals("body", content);
			} else if (content instanceof InputStream) {
				assertEquals(convertStreamToString(attachment.getInputStream()), convertStreamToString((InputStream) content));
			} else if (content instanceof Message) {
				Message message = (Message) content;
				handleMessage(message);
			} else if (content instanceof Multipart) {
				Multipart mp2 = (Multipart) content;
				handleMultipart(mp2);
			}
		}
	}

	private Callable<Boolean> receivedMessagesGreaterThan(int num) {
		return new Callable<Boolean>() {
			public Boolean call() throws Exception {
				MimeMessage[] receivedMessages = smtpServerRule.getMessages();
				return num < receivedMessages.length;
			}
		};
	}

	String convertStreamToString(InputStream is) {
		Scanner s = null;
		try {
		    s = new Scanner(is).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		} finally {
			if (s != null) s.close();
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {}
		}
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
