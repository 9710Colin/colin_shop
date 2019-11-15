package com.wlkg.controller;

import com.wlkg.client.BrandClient;
import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Goods;
import com.wlkg.pojo.PageResult;
import com.wlkg.pojo.SearchRequest;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 12:52
 * @Description:
 */
@RestController
public class SearchController {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsService goodsService;


    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand= brandClient.queryBrandById(id);
        return ResponseEntity.ok(brand);

    }
    //根据关键词搜索
    //请求方式：POST
    //url /page
    //请求参数 key page(当前页码 要分页) size
    //返回类型 PageResult<Goods>  封装了集合，集合里装的是当前分页的数据类型，总记录数和总页数
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){
        PageResult<Goods> result =goodsService.search(request);
        return ResponseEntity.ok(result);
    }

}
