package com.wangyang.service.service.impl;

import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.ICommonService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.vo.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@TemplateOption
public class CommonServiceImpl implements ICommonService {


    @Autowired
    ICategoryService categoryService;
    @Autowired
    ITemplateService templateService;

    @Override
//    @TemplateOptionMethod(name = "Footer",templateValue = "templates/components/@footer",viewName="footer",path = "components")
    public Map<String, String> footer() {
        return null;
    }
//    @TemplateOptionMethod(name = "Index",templateValue = "templates/components/@index",viewName="index",event = "ACAU")
    public IndexVo index() {
        List<CategoryDto> recommend = categoryService.listRecommend();
        List<Template> templates = templateService.listByAndStatusTrue(TemplateType.CATEGORY);
        IndexVo indexVo = new IndexVo();
        indexVo.setTemplates(templates);
        indexVo.setRecommend(recommend);
        return indexVo;
    }



}
