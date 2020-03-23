package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.entity.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "0")
public class Category  extends BaseCategory implements Serializable {

}
