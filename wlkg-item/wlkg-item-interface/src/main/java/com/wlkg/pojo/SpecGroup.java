package com.wlkg.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/29 19:04
 * @Description:
 */
@Table(name = "tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> params; // 该组下的所有规格参数集合

}
