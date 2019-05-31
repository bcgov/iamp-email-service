package ca.bc.gov.iamp.email.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ca.bc.gov.iamp.api.exception.BadRequestException;
import ca.bc.gov.iamp.api.exception.ServiceInternalException;
import ca.bc.gov.iamp.email.model.Mail;

@Component
public class EmailService {

	private final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender sender;

	@Async("threadPoolTaskExecutor")
	public void sendMail(Mail mail) {
		try {
			log.info("Sending notification to [{}]", mail.getTo());
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSubject(mail.getSubject());
			helper.setText(mail.getBody());
			helper.setTo(mail.getTo().toArray(new String[0]));
			helper.setFrom(mail.getFrom());
			helper.setCc(mail.getCc() != null ? mail.getCc().toArray(new String[0]) : null);
			helper.setBcc(mail.getBcc() != null ? mail.getBcc().toArray(new String[0]) : null);
			addAttachments(mail, helper);

			sender.send(message);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
			throw new BadRequestException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceInternalException(e.getMessage());
		}
	}

	private void addAttachments(Mail mail, MimeMessageHelper helper) throws MessagingException {
		List<MultipartFile> attachments = mail.getAttachments();
		if (attachments != null) {
			for (MultipartFile file : attachments) {
				helper.addAttachment(file.getName(), file);
			}
		}
	}
}
