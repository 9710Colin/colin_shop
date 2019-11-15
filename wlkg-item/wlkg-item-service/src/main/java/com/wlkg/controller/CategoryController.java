package com.wlkg.controller;

import com.wlkg.pojo.Category;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 16:15
 * @Description:
 */
@RestController
@RequestMapping("/category")
public class  CategoryController {
    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/list")
    private ResponseEntity<List<Category>> quaryCategoryBypid(@RequestParam(name = "pid") Long pid){
        List<Category> list=categoryService.quaryCategoryBypid(pid);
        return ResponseEntity.ok(list);
    }

    //修改
    @PutMapping("/edit")
    public int editCategory(@RequestParam(name = "id") Long id,@RequestParam(name = "name") String name){
        Category category= categoryService.selectOne(id);
        category.setName(name);
        return categoryService.edit(category);
    }
    //删除
    @DeleteMapping("/del")
    public void deleteCategory(@RequestParam("id") Long id){
        Category category = categoryService.selectOne(id);
        categoryService.recursiveDelete(id);
        categoryService.orUpdateParentNode(category.getParentId());
    }

    @PostMapping("/add")
    public void addCategories(Category category){
        categoryService.addCategory(category);
    }

    /**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = categoryService.queryBycBrandId(bid);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据Id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

}
