package com.wangyang.common.utils;

import com.wangyang.pojo.entity.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class JpaUtils {

    public static PageRequest articleListPageRequest(int page, Category category){
        Sort sort;
        if(category.getDesc()){
            sort= Sort.by(Sort.Direction.DESC, "order","id");
        }else {
            sort= Sort.by(Sort.Direction.ASC, "order","id");
        }

        PageRequest pageRequest = PageRequest.of(page,category.getArticleListSize(), sort);
        return pageRequest;
    }
}
