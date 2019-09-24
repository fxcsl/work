package com.sinovatio.middle;

import com.sinovatio.middle.config.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    private static Logger LOG = LoggerFactory.getLogger(GatewayApplication.class);

    @Autowired
    private Services services;

    @Autowired
    private Env env;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        LOG.info("初始化网关完成！！");
    }

    /**
     * 转发web服务
     * @param builder
     * @return
     */
//    @Bean
//    public RouteLocator webRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                //增加一个path匹配，以"/web/"开头的请求都在此路由
//                .route(r -> r.path("/webc/**")
//                        //表示将路径中的第一级参数删除，用剩下的路径与provider的路径做拼接，
//                        //这里就是"lb://web/hello/"，能匹配到web的HelloController的路径
//                        .filters(f ->
//                                f.stripPrefix(0)
//                                 .filter(new BusPacketCheckFilter()) //过滤header中包含特定字段的请求
//                                 .addRequestHeader("extendtag", "geteway-" + System.currentTimeMillis())
//                        )
//                        //指定匹配服务provider，lb是load balance的意思
////                        .uri("lb://web")
//                        .uri(env.getServicePreStr()+services.web)
//                ).build();
//    }


//    /**
//     * 转发service服务
//     * @param builder
//     * @return
//     */
//    @Bean
//    public RouteLocator serviceRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/service/**")
//                        .filters(f -> f.stripPrefix(0))
////                       .uri("lb://service")
//                        .uri(env.getServicePreStr()+services.service)
//                ).build();
//    }




}
