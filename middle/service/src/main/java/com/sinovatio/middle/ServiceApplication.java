package com.sinovatio.middle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

    private static Logger LOG = LoggerFactory.getLogger(ServiceApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(ServiceApplication.class, args);
        LOG.info("初始化service完成！！");
    }

}
