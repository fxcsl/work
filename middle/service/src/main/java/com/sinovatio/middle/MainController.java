package com.sinovatio.middle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 提供服务的controller
 */
@RestController
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    private final String hostName = System.getenv("HOSTNAME");

    @RequestMapping("/")
    public String home() {
        LOG.info("hostName of {}", hostName);
        return "hello,service! this hostname is " + hostName;
    }

    /**
     * 返回hostname
     *
     * @return 当前应用所在容器的hostname.
     */
    @RequestMapping("/name")
    public String getName() {
        return this.hostName
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
