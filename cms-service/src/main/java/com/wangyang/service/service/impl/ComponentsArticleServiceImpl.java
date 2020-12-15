package com.wangyang.service.service.impl;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.service.repository.ComponentsArticleRepository;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.IComponentsArticleService;
import com.wangyang.service.service.IComponentsService;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.ComponentsArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentsArticleServiceImpl implements IComponentsArticleService {

    @Autowired
    IArticleService articleService;

    @Autowired
    IComponentsService componentsService;

    @Autowired
    ComponentsArticleRepository componentsArticleRepository;

    @Override
    public ComponentsArticle add(int articleId, int componentsId){
        Article article = articleService.findArticleById(articleId);
        Components components = componentsService.findById(componentsId);
        ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(article.getId(), componentsId);
        if(findComponentsArticle!=null){
            throw new ObjectException(article.getTitle()+"已经在组件"+components.getName()+"中！！！");
        }
        ComponentsArticle componentsArticle = new ComponentsArticle();
        componentsArticle.setArticleId(article.getId());
        componentsArticle.setComponentId(components.getId());
        return  componentsArticleRepository.save(componentsArticle);
    }

    @Override
    public ComponentsArticle add(String viewName, int componentsId){
        Article article = articleService.findByViewName(viewName);
        Components components = componentsService.findById(componentsId);

        if(article==null){
            throw new ObjectException("要添加的文章不存在！！");
        }
        if(components==null){
            throw new ObjectException("要添加的组件不存在！！");
        }
        if(components.getDataName().equals(CmsConst.ARTICLE_DATA)){
            ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(article.getId(), componentsId);
            if(findComponentsArticle!=null){
                throw new ObjectException("["+article.getTitle()+"]已经在组件["+components.getName()+"]中！！！");
            }
            ComponentsArticle componentsArticle = new ComponentsArticle();
            componentsArticle.setArticleId(article.getId());
            componentsArticle.setComponentId(components.getId());
            return  componentsArticleRepository.save(componentsArticle);
        }
        throw new ObjectException("文章["+article.getTitle()+"]不能添加到组件["+components.getName()+"]中");
    }

    @Override
    public void delete(int id){
        componentsArticleRepository.deleteById(id);
    }

    @Override
    public ComponentsArticle delete(int articleId, int componentsId){
        ComponentsArticle findComponentsArticle = componentsArticleRepository.findByArticleIdAndComponentId(articleId, componentsId);
        if(findComponentsArticle!=null){
            componentsArticleRepository.deleteById(findComponentsArticle.getId());

        }
        return findComponentsArticle;
    }




}
