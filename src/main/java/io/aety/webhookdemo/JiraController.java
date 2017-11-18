package io.aety.webhookdemo;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
class JiraController {

    private static final Logger log = LoggerFactory.getLogger(JiraController.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${jira.host}")
    private String jiraHost;

    @Value("${jira.port}")
    private String jiraPort;

    @Value("${jira.root}")
    private String jiraRoot;

    @Value("${jira.username}")
    private String jiraUsername;

    @Value("${jira.password}")
    private String jiraPassword;

    @RequestMapping(value = "/rest/webhooks/webhook1", method = RequestMethod.POST)
    @ResponseBody
    void webhook(@RequestBody String body) {
        log.info("Recived json...:" + body);
    }

    @RequestMapping(value = "/rest/webhooks/create", method = RequestMethod.GET)
    void createWebhook() {

        RestTemplate restTemplate = new RestTemplate();

        String plainCreds = jiraUsername + ":" + jiraPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);

//        String uri = new String("http://localhost:2990/jira/rest/api/2/status");
//        HttpEntity<String> request = new HttpEntity<String>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//        String account = response.getBody();
//        log.info("json..:" + account);

        // ******************************
        String uri2 = "http://" + jiraHost + ":" + jiraPort + jiraRoot + "/rest/webhooks/1.0/webhook";
        String requestJson = "{\n" +
                "  \"name\": \"aetys first webhook\",\n" +
                "  \"url\": \"http://localhost:" + serverPort + "/rest/webhooks/webhook1\",\n" +
                "  \"events\": [\n" +
                "    \"jira:issue_created\",\n" +
                "    \"jira:issue_updated\"\n" +
                "  ],\n" +
//                "  \"jqlFilter\": \"Project = TEST\",\n" +
                "  \"excludeIssueDetails\" : false\n" +
                "}";

        HttpEntity<String> request2 = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri2, request2, String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.CREATED) {
            String result = responseEntity.getBody();
            log.info("result..:" + result);
        } else
            log.info("Got status...: " + statusCode);
    }
}
