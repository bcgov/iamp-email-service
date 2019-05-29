package ca.bc.gov.iamp.email.api.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ca.bc.gov.iamp.email.api.EmailsApi;

@RestController
@RequestMapping(path = "/api/v1")
public class EmailsApiImpl implements EmailsApi {

	@Override
	public ResponseEntity<Void> emailsPost(List<String> to, String subject, List<String> cc, List<String> bcc,
			String body, List<MultipartFile> attachments) {
		// TODO Auto-generated method stub
		return EmailsApi.super.emailsPost(to, subject, cc, bcc, body, attachments);
	}
	
}
