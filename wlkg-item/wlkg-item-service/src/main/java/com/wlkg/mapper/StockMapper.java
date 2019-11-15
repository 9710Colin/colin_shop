package com.wlkg.mapper;

import com.wlkg.pojo.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 17:32
 * @Description:
 */
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock> {
}
