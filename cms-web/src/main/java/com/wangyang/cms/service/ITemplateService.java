package com.wangyang.cms.service;

import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.pojo.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITemplateService {
    Optional<Template> findOptionalById(int id);

    List<Template> saveAll(List<Template> templates);
    Template add(Template template);
    Template update(int id,Template updateTemplate);

    List<Template> findAll();

    void deleteById(int id);
    Template findById(int id);
    Page<Template> list(Pageable pageable);

    List<Template> listByAndStatusTrue(TemplateType templateType);

    Template findByEnName(String enName);

    Optional<Template> findOptionalByEnName(String enName);

    void deleteAll();
    List<Template> findByTemplateType(TemplateType type);

    Template setStatus(int id);
}
