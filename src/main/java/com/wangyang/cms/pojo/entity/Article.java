package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseArticle;
import com.wangyang.cms.pojo.entity.base.BaseEntity;
import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "0")
public class Article extends BaseArticle {

}
