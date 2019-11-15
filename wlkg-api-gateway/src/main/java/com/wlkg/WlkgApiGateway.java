package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Auther: Colin
 * @Date: 2019/10/21 14:30
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class WlkgApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(WlkgApiGateway.class, args);
    }
}
