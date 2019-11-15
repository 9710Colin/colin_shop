package com.wlkg.service;

import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.mapper.SpecGroupMapper;
import com.wlkg.mapper.SpecParamMapper;
import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Colin
 * @Date: 2019/10/29 19:11
 * @Description:
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

   /* public List<SpecGroup> querySpecGroups(Long cid) {
        System.out.println("----" + cid);
        SpecGroup t = new SpecGroup();
        t.setCid(cid);
        return this.specGroupMapper.select(t);
    }*/
    /**
     * 根据分类id查询规格组信息
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(list)){
            throw  new WlkgException(ExceptionEnums.SPECIFICATION_GROUP_IS_EMPTY);
        }
        return list;
    }

    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> groups = this.querySpecGroups(cid);
        // 查询当前分类下的规格参数
        List<SpecParam> specParams = querySpecParams(null, cid, null, null);
        Map<Long, List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : specParams){
            if(!map.containsKey(param.getGroupId())){
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }

            map.get(param.getGroupId()).add(param);
        }
        //填充param到group
        for (SpecGroup specGroup: groups){
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return groups;
    }

    /**
     * 根据规则组编号查询规格属性
     * @param gid
     * @return
     */
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam t = new SpecParam();
        if (gid != null) {
            t.setGroupId(gid);
        }
        if (cid != null) {
            t.setCid(cid);
        }
        if (searching != null) {
            t.setSearching(searching);
        }
        if (generic != null) {
            t.setGeneric(generic);
        }

        List<SpecParam> list = specParamMapper.select(t);
        if (CollectionUtils.isEmpty(list)) {
            throw new WlkgException(ExceptionEnums.SPECIFICATION_PARAMS_IS_EMPTY);
        }
        return list;
    }

    public void editGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKeySelective(specGroup);
        System.out.println(specGroup.toString());
    }

    public void saveGroup(SpecGroup specGroup) {
        specGroupMapper.insertSelective(specGroup);

    }

    public void editParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    public void saveParam(SpecParam specParam) {
        specParamMapper.insertSelective(specParam);
    }

    //递归删除
    public void deleteGroup(Long id) {
        specParamMapper.deleteParam(id);
        specGroupMapper.deleteByPrimaryKey(id);
    }

    public void deleteParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }
}

