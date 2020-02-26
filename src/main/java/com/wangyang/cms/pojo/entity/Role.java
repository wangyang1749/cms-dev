package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseEntity;


public class Role extends BaseEntity {
    private int id;
    private String name;
    private String zhName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }
}
