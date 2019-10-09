package com.sinovatio.middle;


import brave.Tracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Component
public class TFilter implements GlobalFilter, Ordered {

    private static final Log LOG = LogFactory.getLog(TFilter.class);

    @Autowired
    Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String token = exchange.getRequest().getQueryParams().getFirst("authToken");
//        if (token == null || token.isEmpty()) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }

        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        for(Map.Entry<String, List<String>> entry : httpHeaders.entrySet()){
            String mapKey = entry.getKey();
            List<String> mapValue = entry.getValue();
            System.out.println("<header>"+mapKey+":"+mapValue.get(0));
        }

        tracer.currentSpan().tag("msg","I'm from gateway!");
        LOG.info("trace info:" + tracer);

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return -100;
    }
}
