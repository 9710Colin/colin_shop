package com.wlkg.client;

import com.wlkg.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/7 18:36
 * @Description:
 */
@FeignClient(name = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
