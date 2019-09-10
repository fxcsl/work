package com.sinovatio.middle;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Services {
    @Value("${service.gateway}")
    public String gateway;

    @Value("${service.web}")
    public String web;

    @Value("${service.service}")
    public String service;

}
