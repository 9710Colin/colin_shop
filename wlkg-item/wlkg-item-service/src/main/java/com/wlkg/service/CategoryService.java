package com.wlkg.service;

import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.mapper.CategoryMapper;
import com.wlkg.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 16:17
 * @Description:
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据pid查询节点
     *
     * @param pid
     * @return
     */
    public List<Category> quaryCategoryBypid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)) {
            //空数据
            throw new WlkgException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    //根据id查询一个对象
    public Category selectOne(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public int edit(Category category) {
        return categoryMapper.updateByPrimaryKeySelective(category);
    }
    //递归删除
    public void recursiveDelete(Long id) {
        List<Category> list = getListByParentId(id);
        if (list.size() == 0) {
            Category deleNode = categoryMapper.selectByPrimaryKey(id);
            Long parentId = deleNode.getParentId();
            categoryMapper.deleteByPrimaryKey(id);
            this.orUpdateParentNode(parentId);
        } else {
            categoryMapper.deleteByPrimaryKey(id);
            for (Category category : list) {
                if (category.getIsParent()) {
                    //递归
                    this.recursiveDelete(category.getId());
                }else {
                    categoryMapper.deleteByPrimaryKey(category.getId());
                }
            }
        }
    }

    //判断该节点的父节点是否需要更新成子节点
    public void orUpdateParentNode(Long parentId) {
        List<Category> listByParentId = getListByParentId(parentId);
        if (listByParentId.size() == 0) {
            Category category = categoryMapper.selectByPrimaryKey(parentId);
            category.setIsParent(false);
            categoryMapper.updateByPrimaryKeySelective(category);
        }
    }

    // 根据节点id 查询该节点下的子节点
    private List<Category> getListByParentId(Long parentId) {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        List<Category> list = categoryMapper.selectByExample(example);
        return list;
    }

    //添加分类
    public void addCategory(Category category) {
        categoryMapper.insertSelective(category);
        Long id = category.getParentId();//父类id
        Category category1 = categoryMapper.selectByPrimaryKey(id);
        category1.setIsParent(true);
//      System.out.println(category.toString());
        categoryMapper.updateByPrimaryKeySelective(category1);
    }


    public List<Category> queryBycBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 根据多个主键查询
     *
     * @param ids
     * @return
     */
    public List<String> quaryNameByIds(List<Long> ids) {
        return categoryMapper.selectByIdList(ids).stream().map(Category::getName).collect(Collectors.toList());
    }

    /**
     * 根据分类id集合查询每个分类对象
     *
     * @param ids
     * @return
     */
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)){
            new WlkgException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return list;
    }




    /*select ts.id, ts.title, CONCAT(tc1.`name`,'/',tc2.`name`,'/',tc3.`name`) cname,tb.`name` from tb_spu ts
	inner join tb_category tc1
	on ts.cid1=tc1.id
	inner join tb_category tc2
	on ts.cid2=tc2.id
	inner join tb_category tc3
	on ts.cid3=tc3.id
	INNER JOIN tb_brand tb
	on ts.brand_id=tb.id*/
}


