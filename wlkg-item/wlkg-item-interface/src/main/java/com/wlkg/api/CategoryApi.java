package com.wlkg.api;

import com.wlkg.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 13:17
 * @Description:
 */
public interface CategoryApi {

    @GetMapping("/category/list/ids")
    public List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);
}
