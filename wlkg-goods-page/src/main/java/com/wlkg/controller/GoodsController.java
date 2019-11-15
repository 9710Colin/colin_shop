package com.wlkg.controller;

import com.wlkg.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Auther: Colin
 * @Date: 2019/11/7 17:23
 * @Description:
 */
@Controller
public class GoodsController {

    @Autowired
    private PageService pageService;

    //显示商品详情
    //请求方式：GET
    //请求url：/item/{id}.html
    //请求参数：id
    //返回类型：String
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long id, ModelMap map){
        //查询数据模型数据
        Map<String, Object> attributes  = pageService.loadModel(id);
        //准备数据模型

        map.addAllAttributes(attributes);
        //判断是否需要生成新的页面
        if (!pageService.exists(id)){
            pageService.syncCreateHtml(id);
        }
        return "item";
    }

}
