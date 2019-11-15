package com.wlkg.controller;

import com.wlkg.pojo.PageResult;
import com.wlkg.pojo.Sku;
import com.wlkg.pojo.Spu;
import com.wlkg.pojo.SpuDetail;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 16:35
 * @Description:
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //商品新增
    //1、请求方式 POST
    //2、请求的参数 spu对象
    //3、url /goods
    //4、返回类型 ResponseEntity<void>
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    //分页查询商品
    //1、请求方式：get
    //2、请求参数 。。。
    //3、url：/spu/page
    //4、返回： ResponseEntity<PageResult<Spu>>
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        // 分页查询spu信息
        PageResult<Spu> result = this.goodsService.querySpuByPageAndSort(page, rows, saleable, key);
        return ResponseEntity.ok(result);
    }

    /*    请求方式：GET
    - 请求路径：/spu/detail/{id}
    - 请求参数：id，应该是spu的id
    - 返回结果：SpuDetail对象*/
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySupDetailById(@PathVariable("id") Long id) {

        SpuDetail detail = goodsService.querySupDetailById(id);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }

    //查询当前商品的所有sku信息
    //1、请求方式：GET
    //2、请求的参数：spuId
    //3、url：/sku/list
    //4、返回类型：ResponseEntity<List<Sku>>
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = goodsService.querySkuBySpuId(id);
        if (skus == null || skus.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }
    //根据spuId查询Spu对象
    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }


    //修改商品
    //1、请求方式：PUT
    //2、请求的参数：Spu对象
    //3、url：/goods
    //4、返回类型：ResponseEntity<Void>
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.update(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //查询商品详情
    //1、请求方式：GET
    //2、请求的参数：spuId
    //3、url：/spu/detail/
    //4、返回类型：ResponseEntity<Spu_detail>
    @DeleteMapping("/spu/delete/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") Long id){
        goodsService.deleteGoodsById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
