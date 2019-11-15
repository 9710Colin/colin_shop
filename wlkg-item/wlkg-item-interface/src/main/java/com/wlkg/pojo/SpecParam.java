package com.wlkg.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: Colin
 * @Date: 2019/10/29 19:16
 * @Description:
 */
@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}
