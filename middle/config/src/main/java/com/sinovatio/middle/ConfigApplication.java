package com.sinovatio.middle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(DummyConfig.class)
@EnableConfigServer
public class ConfigApplication {

    @Autowired
    private DummyConfig dummyConfig;

    @GetMapping("/hello")
    public String hello() {
        return dummyConfig.getMessage()
                + " ["
                + new SimpleDateFormat().format(new Date())
                + "]";
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
