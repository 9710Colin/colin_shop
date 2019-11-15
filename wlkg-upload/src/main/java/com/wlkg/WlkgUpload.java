package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: Colin
 * @Date: 2019/10/28 10:36
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WlkgUpload {
    public static void main(String[] args) {
        SpringApplication.run(WlkgUpload.class,args);
    }
}
