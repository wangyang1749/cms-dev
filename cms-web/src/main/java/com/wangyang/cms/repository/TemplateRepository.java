package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TemplateRepository  extends JpaRepository<Template,Integer>
        , JpaSpecificationExecutor<Template> {
    List<Template> findByTemplateType(TemplateType type);

    Template findByEnName(String enName);

}
