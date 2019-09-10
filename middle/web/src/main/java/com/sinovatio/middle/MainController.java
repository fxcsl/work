package com.sinovatio.middle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    private final String hostName = System.getenv("HOSTNAME");

    @Autowired
    private AccessService accessService;


    @RequestMapping("/")
    public String ribbonPing(HttpServletRequest request){
        LOG.info("hostName of {}", hostName);
        return "hello,web! this hostname is "+hostName;
    }

    /**
     * 返回hostname
     * @return 当前应用所在容器的hostname.
     */
    @RequestMapping("/name")
    public String getName() {
        return this.hostName
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 直接访问service
     * @param request
     * @return
     */
    @RequestMapping(value = "accessService")
    public String accessService(HttpServletRequest request){
        LOG.info("request accessService!");

        StringBuilder sbud = new StringBuilder();

        for(int i=0;i<5;i++){
            sbud.append(accessService.accessService())
                    .append("<br>");
        }

        return sbud.toString();
    }


    /**
     * 通过网关访问
     * @param request
     * @return
     */
    @RequestMapping(value = "accessServiceFromGateway")
    public String accessServiceFromGateway(HttpServletRequest request){
        LOG.info("request accessServiceFromGateway!");
        StringBuilder sbud = new StringBuilder();

        for(int i=0;i<5;i++){
            sbud.append(accessService.accessServiceFromGateway())
                    .append("<br>");
        }

        return sbud.toString();
    }


    /**
     * 返回发现的所有服务
     * @return
     */
    @RequestMapping("/services")
    public String services() {
        LOG.info("request services!");
        return this.discoveryClient.getServices().toString()
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


}
