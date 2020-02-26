package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.TemplateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITemplateService {
    List<Template> saveAll(List<Template> templates);
    Template add(Template template);
    Template update(int id,Template updateTemplate);
    void deleteById(int id);
    Template findById(int id);
    Page<Template> list(Pageable pageable);

    List<Template> findByTemplateType(TemplateType type);
}
