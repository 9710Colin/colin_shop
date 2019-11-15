package com.wlkg.client;

import com.wlkg.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 11:52
 * @Description:查询spu 根据分页查
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {

}

