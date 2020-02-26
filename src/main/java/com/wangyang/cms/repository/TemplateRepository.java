package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository  extends JpaRepository<Template,Integer> {
    List<Template> findByTemplateType(TemplateType type);
}
