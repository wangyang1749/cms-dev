package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.ChannelDto;
import com.wangyang.cms.pojo.dto.SheetDto;
import com.wangyang.cms.pojo.entity.Channel;
import com.wangyang.cms.pojo.entity.Sheet;

import java.util.List;

public class ChannelVo extends ChannelDto {

    List<ArticleDto> articles;

    public List<ArticleDto> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDto> articles) {
        this.articles = articles;
    }
}
