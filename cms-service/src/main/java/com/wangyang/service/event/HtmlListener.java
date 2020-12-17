package com.wangyang.service.event;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.pojo.entity.Category;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HtmlListener {


    @EventListener
    public void createArticle(EntityCreatedEvent<Category> event){
        Category category = (Category) event.getSource();
        //创建/更新 文章-删除文章分页的缓存文件
//        FileUtils.removeCategoryPageTemp(category);
        //移除临时文章分类
        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
        FileUtils.remove(CmsConst.WORK_DIR+"/html/mind/"+category.getId()+".html");
        System.out.println("移除临时文章..... ");
    }
}
