package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: Colin
 * @Date: 2019/11/7 17:08
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WlkgGoodsPage {
    public static void main(String[] args) {
        SpringApplication.run(WlkgGoodsPage.class,args);
    }
}
