package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.params.ComponentsParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IComponentsService {

    Page<Components> list(Pageable pageable);

    Components add(Components templatePage);

    List<Components> addAll(List<Components> templatePages);
    Components update(int id, ComponentsParam templatePageParam);

    Components findById(int id);

    void delete(int id);
    void deleteAll();


    Object getModel(String dataName);

    Components findByDataName(String dataName);
}
