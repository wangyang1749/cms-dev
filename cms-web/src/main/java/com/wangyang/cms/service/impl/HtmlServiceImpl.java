package com.wangyang.cms.service.impl;

import com.wangyang.cms.config.CmsConfig;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ChannelVo;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.repository.ChannelRepository;
import com.wangyang.cms.repository.ComponentsRepository;
import com.wangyang.cms.service.*;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HtmlServiceImpl implements IHtmlService {

    @Autowired
    ITemplateService templateService;
    @Autowired
    ICategoryService categoryService;

    @Autowired
    ComponentsRepository componentsRepository;
    @Autowired
    IArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    IOptionService optionService;
    @Autowired
    ISheetService sheetService;

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    IChannelService channelService;
    @Autowired
    IComponentsService componentsService;
    @Override
    public void conventHtml(ArticleDetailVO articleVO){
        if(articleVO.getStatus()== ArticleStatus.PUBLISHED){
            BaseCategory baseCategory = articleVO.getCategory();

            if(baseCategory instanceof  Category){
                //如果是列表类型的分类
                Category category = (Category)baseCategory;
                covertHtml(articleVO);
                log.info("!!### generate "+articleVO.getViewName()+" html success!!");
                convertHtml(category);
//                addOrRemoveArticleToCategoryListByCategoryId(articleVO.getCategoryId());

            }else {
                //如果是内容类型的栏目
                Channel channel = (Channel)baseCategory;

                ChannelVo channelVo = conventHtml(channel);

                Template articleTemplate = templateService.findByEnName(channelVo.getArticleTemplateName());
                TemplateUtil.convertHtmlAndSave(articleVO,articleTemplate);
            }
        }
    }


    @Override
    public void addOrRemoveArticleToCategoryListByCategoryId(int baseCategoryId) {
        Optional<BaseCategory> baseCategoryOptional = categoryService.findBaseCategoryOptionalById(baseCategoryId);
        if(baseCategoryOptional.isPresent()){
            BaseCategory baseCategory = baseCategoryOptional.get();
            if(baseCategory instanceof Category){
                Category category = (Category)baseCategory;
                convertHtml(category);
            }else {
                Channel channel = (Channel)baseCategory;
                conventHtml(channel);

            }
        }
    }

    /**
     * 生成该栏目下所有文章的列表, 用于动态添加到文章详情的旁边
     * @param channel
     * @return
     */
    @Override
    public ChannelVo conventHtml(Channel channel){
        ChannelVo channelVo = channelService.convent(channel);
        Template template = templateService.findByEnName(channelVo.getTemplateName());
        TemplateUtil.convertHtmlAndSave(channelVo,template);
        return channelVo;
    }
    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    @Override
    public CategoryArticleListDao convertHtml(Category category) {
        CategoryArticleListDao categoryArticleListDao = articleService.getArticleListByCategory(category);
        log.debug("生成"+category.getName()+"分类下的第一个页面!");
//        Template template = templateService.findById(category.getTemplateId());
        Optional<Template> template = templateService.findOptionalByEnName(category.getTemplateName());
        if(template.isPresent()){
            TemplateUtil.convertHtmlAndSave(categoryArticleListDao,template.get());
        }
        if(category.getRecommend()&&category.getHaveHtml()){
            generateHome();
        }
        return categoryArticleListDao;
    }
    private String covertHtml(ArticleDetailVO articleDetailVO) {
//        Template template = templateService.findById(articleDetailVO.getTemplateId());
        Template template = templateService.findByEnName(articleDetailVO.getTemplateName());
        String html = TemplateUtil.convertHtmlAndSave(articleDetailVO, template);
        return html;
    }


    @Override
    public void convertHtml(Sheet sheet) {
        Template template = templateService.findByEnName(sheet.getTemplateName());
        TemplateUtil.convertHtmlAndSave(sheet,template);
    }


    @Override
    public Components generateHome(){
        Components components = componentsService.findByDataName("commonServiceImpl.index");
        Object data = getData(components.getDataName());
        TemplateUtil.convertHtmlAndSave(data,components);
        return components;
    }


    @Override
    public void generateCategoryListHtml() {
        Components components = componentsService.findByDataName("categoryServiceImpl.list");
        Object data = getData(components.getDataName());
        TemplateUtil.convertHtmlAndSave(data, components);
    }
    @Override
    public void generateChannelListHtml() {
        Components components = componentsService.findByDataName("channelServiceImpl.list");
        Object data = getData(components.getDataName());
        TemplateUtil.convertHtmlAndSave(data, components);
    }

    @Override
    public void generateMenuListHtml() {
        Components components = componentsService.findByDataName("menuServiceImpl.list");
        Object data = getData(components.getDataName());
        TemplateUtil.convertHtmlAndSave(data,components);
    }






    @Override
    public void commonTemplate(String option){
        List<Components> templatePages = componentsRepository.findAll((Specification<Components>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.like(root.get("event"), "%"+option+"%"),
                        criteriaBuilder.isTrue(root.get("status"))
                ).getRestriction());

        templatePages.forEach(templatePage -> {
            Object data = getData(templatePage.getDataName());
            if(data!=null){
                TemplateUtil.convertHtmlAndSave(data,templatePage);
            }
        });
    }

    public Object getData(String name){
        try {
            String[] names = name.split("\\.");
            String className = names[0];
            String methodName = names[1];
            Object bean = CmsConfig.getBean(className);
            Method method = bean.getClass().getMethod(methodName);
            return method.invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void generateSheetListByChannelId(int id) {
        Optional<Channel> channel = channelRepository.findById(id);
        if(channel.isPresent()){
            ChannelVo channelVo = channelService.convent(channel.get());
//            Template template = templateService.findById(channelVo.getTemplateId());
            Template template = templateService.findByEnName(channelVo.getTemplateName());
            //生成列表
            TemplateUtil.convertHtmlAndSave(channelVo,template);
        }

    }

}
