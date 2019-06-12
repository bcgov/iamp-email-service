IAMP Email Service
==================

The email service is a standalone service which can receive rest payloads and build/send emails.


### How to send emails using Java (Spring / Rest Template)

The code block below shows an example of how to build and send a valid payload for the email service:

`

    @Value("classpath:/sample/test-attachment.file")
    private Resource attachment;
    
    ...
    
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
    
    ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/emails"), HttpMethod.POST, entity, String.class);
    
`

The code above can also be useful for automated testing.
