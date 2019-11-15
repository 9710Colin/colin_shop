package com.wlkg.client;

import com.wlkg.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 13:27
 * @Description:
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
