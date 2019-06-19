IAMP Email Service
==================

The email service is a standalone service which can receive rest payloads and build/send emails.


### How to send emails using Java (Spring / Rest Template)

The code block below shows an example of how to build and send a valid payload for the email service:


```
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
```

The code above can also be useful for automated testing.




```
Copyright 2019 Province of British Columbia

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
