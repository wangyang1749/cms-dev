package com.wangyang.data.repository;

import com.wangyang.model.pojo.enums.TemplateType;
import com.wangyang.model.pojo.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TemplateRepository  extends JpaRepository<Template,Integer>
        , JpaSpecificationExecutor<Template> {
    List<Template> findByTemplateType(TemplateType type);

    Template findByEnName(String enName);

}
