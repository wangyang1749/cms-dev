package com.wangyang.cms.core.jms.consumer;

import com.wangyang.cms.core.jms.CmsService;
import com.wangyang.cms.core.jms.producer.DestinationConst;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleConsumerServiceImpl {

    @Autowired
    ITemplateService templateService;
    @Autowired
    ICategoryService categoryService;

    @Autowired
    CmsService cmsService;


    @JmsListener(destination = DestinationConst.ARTICLE_HTML_STRING)
    public void receiveArticleDetailVO(ArticleDetailVO articleVO){
        if(articleVO.getStatus()==ArticleStatus.PUBLISHED){
            log.debug("start article  covertHtml(articleVO) start");
            covertHtml(articleVO);
            log.debug("end article covertHtml(articleVO) end");
            log.info("!!### generate "+articleVO.getViewName()+" html success!!");
            log.debug("start article  addArticleToCategoryList(articleVO.getId()) start");
            addArticleToCategoryList(articleVO.getId());
            log.debug("end  article  addArticleToCategoryList(articleVO.getId())  end");

        }
    }



    private void addArticleToCategoryList(int id) {
        List<Category> categories = categoryService.findCategoryByArticleId(id);
        categories.forEach(category -> {
            CategoryArticleListDao articleListByCategory = cmsService.getArticleListByCategory(category);
            Template template = templateService.findById(category.getTemplateId());
            TemplateUtil.convertHtmlAndSave(articleListByCategory,template);
            log.info("!!### generate category "+articleListByCategory.getViewName()+" html success!!");
        });
    }


    private String covertHtml(ArticleDetailVO articleDetailVO) {

        Template template = templateService.findById(articleDetailVO.getTemplateId());
        String html = TemplateUtil.convertHtmlAndSave(articleDetailVO, template);
        return html;

    }
}
