package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.pojo.params.ComponentsParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface IComponentsService {

    Page<Components> list(Pageable pageable);

    Components add(Components templatePage);

    List<Components> addAll(List<Components> templatePages);
    Components update(int id, ComponentsParam templatePageParam);

    Components findById(int id);

    void delete(int id);
    void deleteAll();

    String generateHtml(int templateId);

    ModelAndView preview(int templateId);

    Components findByDataName(String dataName);
}
