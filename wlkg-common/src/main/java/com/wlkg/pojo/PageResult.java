package com.wlkg.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 22:43
 * @Description:
 */
@Data
public class PageResult<T> {
    private Long total;// 总条数
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }


}

