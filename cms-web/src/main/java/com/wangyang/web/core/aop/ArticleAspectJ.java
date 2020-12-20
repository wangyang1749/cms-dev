package com.wangyang.web.core.aop;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.service.service.IHtmlService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author wangyang
 * @date 2020/12/16
 */

@Component
@Aspect
@Slf4j
public class ArticleAspectJ {

    @Autowired
    IHtmlService htmlService;

    /**
     * 需要执行删除
     */
    @Around("execution(* com.wangyang.web.controller.api.ArticleController.updateArticleDetailVO(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.createArticleDetailVO(..)) or " +
            "execution(* com.wangyang.web.controller.api.ArticleController.delete(..)) or" +
            "execution(* com.wangyang.web.controller.api.ArticleController.updateCategory(..))")
    public ArticleDetailVO test(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object o = joinPoint.proceed();
            ArticleDetailVO articleDetailVO = (ArticleDetailVO)o;
            deleteTemp(articleDetailVO.getCategory());
            if(articleDetailVO.getOldCategory()!=null){
                deleteTemp(articleDetailVO.getOldCategory());
            }
            htmlService.newArticleListHtml();
            log.info(">>> "+articleDetailVO.getCategory().getName()+"的临时文件");
            return articleDetailVO;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 需要执行删除
     */
    @Around("execution(* com.wangyang.web.controller.api.CategoryController.update(..)) or " +
            "execution(* com.wangyang.web.controller.api.CategoryController.deleteById(..)) or" +
            "execution(* com.wangyang.web.controller.api.CategoryController.haveHtml(..)) ")
    public Category categoryAop(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object o = joinPoint.proceed();
            Category category = (Category)o;
            deleteTemp(category);

            return category;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }






    @Async
    public void deleteTemp(Category category){
        log.info("####删除分类分页文件-"+category.getName());
        FileUtils.remove(CmsConst.WORK_DIR+File.separator+category.getPath()+File.separator+category.getViewName());
        //移除临时文章分类
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/mind/"+category.getId()+".html");
    }
}
