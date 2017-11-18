package io.aety.webhookdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WebhookdemoApplication {

    private static final Logger log = LoggerFactory.getLogger(WebhookdemoApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(WebhookdemoApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            String json = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);
            log.info("json....:" + json);
        };
    }
}