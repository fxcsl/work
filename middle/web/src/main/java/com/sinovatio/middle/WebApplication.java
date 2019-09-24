package com.sinovatio.middle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class WebApplication {
    private static Logger LOG = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
        LOG.info("初始化web完成！！");
    }

}
