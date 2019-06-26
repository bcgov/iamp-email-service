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
package ca.bc.gov.iamp.email.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ca.bc.gov.iamp.email.api.EmailsApi;
import ca.bc.gov.iamp.email.model.Attachment;
import ca.bc.gov.iamp.email.model.Mail;
import ca.bc.gov.iamp.email.service.EmailService;

@RestController
@RequestMapping(path = "/api/v1")
public class EmailsApiImpl implements EmailsApi {
	
	private final Logger log = LoggerFactory.getLogger(EmailsApiImpl.class);


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
		List<Attachment> attachmentList = addAttachments(attachments);
		mail.setAttachments(attachmentList);
		
		emailService.sendMail(mail);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<Attachment> addAttachments(List<MultipartFile> attachments) {
		List<Attachment> attachmentList = new ArrayList<>();
		try {
			for (MultipartFile file : attachments) {
				attachmentList.add(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getBytes()));
			}
			return attachmentList;
		} catch (IOException e) {
			log.error("Exception while processing attachments.", e);
			return Collections.emptyList();
		}
	}

}
