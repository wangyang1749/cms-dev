package com.wangyang.service.util;

import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Sheet;

import java.io.File;

/**
 * @author wangyang
 * @date 2020/12/15
 */
public class FormatUtil {
    /**
     * 第一页分类文章列表
     * eg. html_articleList_bioinfo.html
     * @param category
     * @return
     */
    public static String categoryListFormat(Category category){

        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName()+".html";
    }

    /**
     * 第二页分类文章列表
     *  eg. html_articleList_bioinfo_2_page.html
     * @param category
     * @return
     */
    public static String categoryList2Format(Category category) {
        return File.separator+category.getPath().replace(File.separator,"_")+"_"+category.getViewName();
    }
    public static String articleListFormat(Article article) {
        return File.separator+article.getPath().replace(File.separator,"_")+"_"+article.getViewName()+".html";
    }
    public static String sheetListFormat(Sheet sheet) {
        return File.separator+sheet.getPath().replace(File.separator,"_")+"_"+sheet.getViewName()+".html";
    }


}
