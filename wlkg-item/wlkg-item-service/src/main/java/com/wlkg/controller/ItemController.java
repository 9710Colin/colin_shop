package com.wlkg.controller;

import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.pojo.Item;
import com.wlkg.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 11:44
 * @Description:
 */
@RestController
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item) {
        //如果价格为空，则抛出异常，返回400状态码，请求参数有误
        if (item.getPrice() == null) {
            throw new WlkgException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
        }
        item = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }
}
