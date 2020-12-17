package com.wangyang.service.service;

import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.params.ComponentsParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IComponentsService {

    Page<Components> list(Pageable pageable);

    List<Components> listNeedArticle();

    /**
     * 查找所有组件
     * @return
     */
    List<Components> listAll();
    Components add(ComponentsParam componentsParam);


    List<Components> addAll(List<Components> templatePages);
    Components update(int id, ComponentsParam templatePageParam);

    Components findById(int id);

    Components findDetailsById(int id);

    Components delete(int id);
    void deleteAll();


    Map<String ,Object> getModel(Components components);

    Components findByDataName(String dataName);

    Components findByViewName(String viewName);
}
