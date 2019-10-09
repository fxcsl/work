package com.sinovatio.middle;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AccessService {

    @Autowired
    private Services services;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 直接访问目标服务
     *
     * @return
     */
//    @HystrixCommand(fallbackMethod = "getFallbackName", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    public String accessService() {
        return this.restTemplate.getForObject("http://" + services.service + "/name", String.class);
    }

    /**
     * 直接访问目标服务
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "getFallbackName", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    public String accessServiceFromGateway() {
        return this.restTemplate.getForObject("http://" + services.gateway + "/preservice/name", String.class);
    }

    /**
     * 熔断时调用的方法
     *
     * @return
     */
    private String getFallbackName() {
        return "Fallback"
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
