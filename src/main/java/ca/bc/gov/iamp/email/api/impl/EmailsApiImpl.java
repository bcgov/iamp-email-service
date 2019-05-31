package ca.bc.gov.iamp.email.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ca.bc.gov.iamp.email.api.EmailsApi;
import ca.bc.gov.iamp.email.model.Mail;
import ca.bc.gov.iamp.email.service.EmailService;

@RestController
@RequestMapping(path = "/api/v1")
public class EmailsApiImpl implements EmailsApi {


	@Value("${email.notificator.from}")
	private String from;
	
	@Autowired
	private EmailService emailService;

	@Override
	public ResponseEntity<Void> emailsPost(List<String> to, String subject, List<String> cc, List<String> bcc,
			String body, List<MultipartFile> attachments) {
		Mail mail = new Mail();
		mail.setSubject(subject);
		mail.setFrom(from);
		mail.setTo(to);
		mail.setCc(cc);
		mail.setBcc(bcc);
		mail.setBody(body);
		mail.setAttachments(attachments);
		
		emailService.sendMail(mail);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
