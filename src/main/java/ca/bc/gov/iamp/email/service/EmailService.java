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
package ca.bc.gov.iamp.email.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ca.bc.gov.iamp.api.exception.ServiceInternalException;
import ca.bc.gov.iamp.email.model.Attachment;
import ca.bc.gov.iamp.email.model.Mail;

@Component
public class EmailService {

	private final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender sender;

	@Async("threadPoolTaskExecutor")
	public void sendMail(Mail mail) {
		try {
			log.info("Sending email to [{}]", mail.getTo());
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSubject(mail.getSubject());
			helper.setText(mail.getBody());
			helper.setTo(mail.getTo().toArray(new String[0]));
			helper.setFrom(mail.getFrom());
			helper.setCc(mail.getCc() != null ? mail.getCc().toArray(new String[0]) : new String[0]);
			helper.setBcc(mail.getBcc() != null ? mail.getBcc().toArray(new String[0]) : new String[0]);
			addAttachments(mail, helper);

			sender.send(message);
			log.info("Email sent successfully");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceInternalException(e.getMessage());
		}
	}

	private void addAttachments(Mail mail, MimeMessageHelper helper) throws MessagingException {
		List<Attachment> attachments = mail.getAttachments();
		if (attachments != null) {
			for (Attachment file : attachments) {
				helper.addAttachment(file.getOriginalFilename(), new InputStreamSource() {
					@Override
					public InputStream getInputStream() throws IOException {
						return new ByteArrayInputStream(file.getData());
					}
				});
			}
		}
	}
}
