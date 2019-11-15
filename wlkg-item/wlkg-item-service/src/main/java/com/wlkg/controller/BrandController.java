package com.wlkg.controller;

import com.wlkg.pojo.Brand;
import com.wlkg.pojo.PageResult;
import com.wlkg.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 19:27
 * @Description:
 */
@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;

    //请求方式：GET
    //请求参数：page=1,rows=5,key="",sortby="letter", desc=true
    //请求的URL:/brand/page
    //返回类型:List<Brands>, total, totalPages
    @GetMapping("/brand/page")//因为通过前端来调用 所以返回ResponseEntity这种响应类型
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,//当前页
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,//每页大小
            @RequestParam(value = "sortBy", required = false) String sortBy,//排序字段 required = false可以不传参并且可以不写 true必须传参
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,//是否为降序 Boolean
            @RequestParam(value = "key", required = false) String key) {//搜索关键字
        PageResult<Brand> result = brandService.queryBrandByPageAndSort(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(result);
    }

    //增加品牌
    //1、请求方式：POST
    //2、请求的参数：Brand对象，List<Integer> cids
    //3、url：/item/brand
    //4、返回类型：无返回 ResponseEntity<Void>
    @PostMapping("/brand")
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    @PutMapping("/brand")
    public void editBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.editBrand(brand,cids);
    }

    @DeleteMapping("/brand")
    public void delBrand(Long id){
        brandService.delBrand(id);
    }


    //根据分类查询品牌
    //1、请求方式：GET
    //2、请求的参数：分类id
    //3、url：/brand/cid/{cid}
    //4、返回类型：ResponseEntity<List<Brand>>
    @GetMapping("/brand/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid) {
        List<Brand> list = this.brandService.queryBrandByCategory(cid);
        if (CollectionUtils.isEmpty(list)) {
            // 响应404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(list);
    }
    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }

}
