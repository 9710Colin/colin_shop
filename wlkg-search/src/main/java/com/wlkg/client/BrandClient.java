package com.wlkg.client;

import com.wlkg.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 11:56
 * @Description:
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {

}
