package com.wlkg.client;

import com.wlkg.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/7 18:34
 * @Description:
 */
@FeignClient(name = "item-service")
public interface BrandClient extends BrandApi {
}
