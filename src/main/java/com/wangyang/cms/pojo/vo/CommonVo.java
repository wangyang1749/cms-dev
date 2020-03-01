package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonVo {
    Map<String,List<ArticleDto>>  map;
    List<CategoryVO> categories;


    public CommonVo(){
        map = new HashMap<>();
    }

    public  void addArticle(String key,List<ArticleDto> articles){
        map.put(key,articles);
    }

    public Map<String, List<ArticleDto>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<ArticleDto>> map) {
        this.map = map;
    }

    public List<CategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryVO> categories) {
        this.categories = categories;
    }
}
