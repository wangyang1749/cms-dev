package com.wangyang.cms.service.impl;

import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.CommonVo;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@TemplateOption
public class CommonServiceImpl implements ICommonService {

    @Override
    @TemplateOptionMethod(name = "Footer",templateValue = "templates/components/@footer",viewName="footer",path = "components")
    public Map<String, String> footer() {
        return null;
    }
}
