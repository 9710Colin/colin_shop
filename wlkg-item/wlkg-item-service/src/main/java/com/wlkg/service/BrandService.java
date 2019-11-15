package com.wlkg.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlkg.mapper.BrandMapper;
import com.wlkg.pojo.Brand;
import com.wlkg.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.beans.Transient;
import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/24 19:38
 * @Description:
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 实现品牌的分页查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //1、实现分页查询 （后面执行的sql语句都会采用分页）
        PageHelper.startPage(page,rows);
        //2.设置过滤字段
        Example example = new Example(Brand.class);
        if (StringUtil.isNotEmpty(key)){
            example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key);
        }

        //3.设置排序
        if (StringUtil.isNotEmpty(sortBy)){
            String orderByClause = sortBy+(desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        //4. 封装分页对象
        Page<Brand> pageInfo = (Page<Brand>)brandMapper.selectByExample(example);

        return new PageResult<>(pageInfo.getTotal(),pageInfo);
    }

//-------------------------------------------------------------------
    /*    PageHelper.startPage(page,rows);
    //      List<Brand> list = brandMapper.selectAll(); 因为有排序等条件了 所以要换一种方式查询
    Example example = new Example(Brand.class);
    //2、设置过滤字段判断key是否为空 因为有可能不传
    // 如果key=" " isEmpty是false（非空） isBlank是true
        if (!StringUtils.isNotBlank(key)){//表示不为空
        example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key);
    }
    //3、设置排序（StringUtil是commons（apache下的顶级项目）包里的）---点不出来的话就依赖commons-lang3
    //省去了大量的判断代码
        if (StringUtil.isNotEmpty(sortBy)){
        String orderByClause = sortBy+(desc ? " DESC" : " ASC");//ASC升序，DESC降序
        example.setOrderByClause(orderByClause);
    }
    //注意brandMapper.selectByExample(example)返回的结果其实不是整个表的搜索结果，而是当前页的数据
    List<Brand> list = brandMapper.selectByExample(example);//其实真实返回的是Page<>类型 写的话就要强转 不用写是因为Page<>就继承了ArrayList 不一样在于这个Page<>里封装了一些分页的信息 所以也可以不用创建PageInfo对象了 直接用Page接受也行
    //判断集合是否为空 建议使用CollectionUtils spring框架提供
        if (CollectionUtils.isEmpty(list)){
        throw new WlkgException(ExceptionEnums.BRAND_IS_EMPTY);
    }

    PageInfo<Brand> pageInfo = new PageInfo<>(list);
    //4、封装分页对象
    PageResult<Brand> result = new PageResult<>();
        result.setItems(list);
        result.setTotalPage(Long.valueOf(pageInfo.getPages()));//总页数怎么取？创建一个PageInfo对象，通过pageinfo对象调用getPages方法（pages就是总页数）
    //Long.valueOf   -->int->long
        result.setTotal(pageInfo.getTotal());//总记录数？放的应该是当前页的数据,应该从pageInfo里取
        return result;*/

//-------------------------------------------------------------------


    /**
     * 添加新品牌
     * @param brand
     * @param cids
     */
    @Transient
    public void saveBrand(Brand brand, List<Long> cids) {
        //1、新增品牌信息
        brandMapper.insertSelective(brand);
        //2.添加该品牌关联的分类
        for (Long cid : cids) {
            brandMapper.insertCategoryBrand(cid,brand.getId());
        }
    }

    @Transactional
    public void editBrand(Brand brand, List<Long> cids) {
        //更新商品表
        brandMapper.updateByPrimaryKeySelective(brand);
        long bid=brand.getId();
        //删除该商品对应的分类
        brandMapper.deleteCategoryBrand(bid);
        //添加新的分类
        for(Long cid:cids){
            brandMapper.insertCategoryBrand(cid,bid);
        }
    }

    @Transient
    public void delBrand(Long id) {
        brandMapper.deleteByPrimaryKey(id);
        brandMapper.deleteCategoryBrand(id);
    }

    /**
     * 根据分类Id查询品牌
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCategory(Long cid) {
        return this.brandMapper.queryByCategoryId(cid);
    }

    /**
     * 根据品牌Id查询品牌对象
     * @param brandId
     * @return
     */
    public Brand queryBrandById(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }
}
