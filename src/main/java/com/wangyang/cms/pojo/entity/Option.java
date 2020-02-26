package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "options")
public class Option extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * option key
     */
    @Column(name = "option_key", columnDefinition = "varchar(100) not null")
    private String key;
    /**
     * option value
     */
    @Column(name = "option_value", columnDefinition = "varchar(1023) not null")
    private String value;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
