package ca.bc.gov.iamp.email.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import ca.bc.gov.iamp.api.exception.ServiceInternalException;
import ca.bc.gov.iamp.email.model.Attachment;
import ca.bc.gov.iamp.email.model.Mail;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

	@Mock
	private JavaMailSender sender;

	@InjectMocks
	private EmailService emailService;

	@Before
	public void init() {
		initProperties();
		initMocks();
	}

	private void initProperties() {
	}

	private void initMocks() {
		when(sender.createMimeMessage()).thenReturn(new MimeMessage(Session.getInstance(new Properties())));
	}

	@Test(expected = ServiceInternalException.class)
	public void testNullMail() {
		emailService.sendMail(null);
	}

	@Test(expected = ServiceInternalException.class)
	public void testEmptyMail() {
		Mail mail = new Mail();
		emailService.sendMail(mail);
	}

	@Test(expected = ServiceInternalException.class)
	public void testValidMailNoTo() {
		Mail mail = createValidMail();
		mail.setTo(null);
		emailService.sendMail(mail);
		verify(sender, times(1)).send(Mockito.any(MimeMessage.class));
	}
	
	@Test
	public void testValidMailNoAttachments() {
		Mail mail = createValidMail();
		mail.setAttachments(null);
		emailService.sendMail(mail);
		verify(sender, times(1)).send(Mockito.any(MimeMessage.class));
	}

	@Test
	public void testValidMail() {
		Mail mail = createValidMail();
		mail.setAttachments(null);
		emailService.sendMail(mail);
		verify(sender, times(1)).send(Mockito.any(MimeMessage.class));
	}
	
	private Mail createValidMail() {
		Mail mail = new Mail();
		mail.setSubject("subject");
		mail.setBody("body");
		mail.setFrom("from@from.com");
		mail.setTo(Arrays.asList("to1@to.com", "to2@to.com"));
		mail.setCc(Arrays.asList("cc@cc.com"));
		mail.setBcc(Arrays.asList("bcc1@bcc.com", "bcc2@bcc.com"));
		mail.setAttachments(Arrays
				.asList(new Attachment("filename.txt", "text/plain", "awesome email body".getBytes())));
		return mail;
	}
}
