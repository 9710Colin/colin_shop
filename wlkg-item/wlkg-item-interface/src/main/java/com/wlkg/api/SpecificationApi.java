package com.wlkg.api;

import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 13:21
 * @Description:
 */
public interface SpecificationApi {

    //分页查询 规格参数
    @GetMapping("/spec/params")
    public List<SpecParam> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic);



    @GetMapping("/spec/groups/{cid}")
    public List<SpecGroup> querySpecGroups(@PathVariable("cid") Long cid);

}
