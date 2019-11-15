package com.wlkg.service;

import com.wlkg.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 11:43
 * @Description:
 */
@Service
public class ItemService {
    public Item saveItem(Item item){
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}

