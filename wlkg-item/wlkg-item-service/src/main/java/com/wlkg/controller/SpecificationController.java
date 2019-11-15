package com.wlkg.controller;

import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import com.wlkg.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/29 19:10
 * @Description:
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    //1.根据分类查询规格组
    //提交方式：GET
    //url:/spec/groups/
    //参数:Long cid
    //返回类型:List<SpecGroup>
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecGroupsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    //2.根据规格组编号查询规格属性
    /*- 请求方式：GET
    - 请求路径：/item/spec/params?gid=
    - 请求参数：gid，分组id
    - 返回结果：该分组下的规格参数集合`List<SpecParam>`
    */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic){
        List<SpecParam> list = this.specificationService.querySpecParams(gid,cid,searching,generic);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }



    @PutMapping("group")
    public void editGroup(@RequestBody SpecGroup specGroup){
        specificationService.editGroup(specGroup);
    }

    @PostMapping("group")
    public void saveGroup(@RequestBody SpecGroup specGroup){
        specificationService.saveGroup(specGroup);
    }
    @DeleteMapping("group/{id}")
    public void deleteGroup(@PathVariable Long id){
        specificationService.deleteGroup(id);
    }

    @PutMapping("/param")
    public void editParam(@RequestBody SpecParam specParam){
        specificationService.editParam(specParam);
    }

    @PostMapping("/param")
    public void saveParam(@RequestBody SpecParam specParam){
        specificationService.saveParam(specParam);
    }
    @DeleteMapping("/param/{id}")
    public void deleteParam(@PathVariable Long id){
        specificationService.deleteParam(id);
    }

}
