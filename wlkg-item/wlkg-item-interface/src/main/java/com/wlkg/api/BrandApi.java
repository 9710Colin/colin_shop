package com.wlkg.api;

import com.wlkg.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 13:19
 * @Description:
 */
public interface BrandApi {

    @GetMapping("/brand/{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);
}
