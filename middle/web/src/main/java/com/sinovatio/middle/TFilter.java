package com.sinovatio.middle;


import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class TFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(TFilter.class);

    @Autowired
    Tracer tracer;

    @Override
    public void destroy() {
        System.out.println("t Filter is destoried");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                //通过请求头的名称获取请求头的值
                String value = httpRequest.getHeader(name);
                System.out.println("<header>"+name+" : "+value);
            }
        }

        LOG.info("trace info:"+tracer);
        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("t Filter is inited");
    }
}
