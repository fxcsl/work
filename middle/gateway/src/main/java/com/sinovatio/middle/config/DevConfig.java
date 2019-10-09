package com.sinovatio.middle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile({"dev"})
@Configuration
public class DevConfig {

    @Primary
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    Env env() {
        return new Env("dev");
    }

}
