package com.wangyang.data.service.impl;

import com.wangyang.common.exception.TemplateException;
import com.wangyang.data.ApplicationBean;
import com.wangyang.data.service.IComponentsService;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.data.repository.ComponentsRepository;
import com.wangyang.model.pojo.params.ComponentsParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComponentsServiceImpl implements IComponentsService {

    @Autowired
    ComponentsRepository componentsRepository;






    @Override
    public Page<Components> list(Pageable pageable){
        return componentsRepository.findAll(pageable);
    }


    @Override
    public Components add(Components templatePage){
        Components components = componentsRepository.save(templatePage);
        Object o = getModel(components.getDataName());
        log.info("Generate html in "+components.getPath()+"/"+components.getViewName());
        return components;
    }


    @Override
    public Components update(int id, ComponentsParam templatePageParam){
        Components templatePage = findById(id);
        BeanUtils.copyProperties(templatePageParam,templatePage);
        return componentsRepository.save(templatePage);
    }

    @Override
    public List<Components> addAll(List<Components> templatePages) {
        return componentsRepository.saveAll(templatePages);
    }

    @Override
    public Components findById(int id){
        Optional<Components> templatePageOptional = componentsRepository.findById(id);
        if(!templatePageOptional.isPresent()){
            throw new TemplateException("add template did't exist!!");
        }
        return templatePageOptional.get();
    }

    @Override
    public void delete(int id){
        componentsRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        componentsRepository.deleteAll();
    }





    @Override
    public Object getModel(String dataName) {
        try {
            String[] names = dataName.split("\\.");
            String className = names[0];
            String methodName = names[1];
            Object bean = ApplicationBean.getBean(className);
            Method method = bean.getClass().getMethod(methodName);
            Object o = method.invoke(bean);
            return o;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Components findByDataName(String dataName){
        List<Components> templatePages = componentsRepository.findAll(new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("dataName"), dataName)
                        ,criteriaBuilder.isTrue(root.get("status"))).getRestriction();
            }
        });

        if(CollectionUtils.isEmpty(templatePages)){
            throw new TemplateException("Template Not found!!");
        }

        return templatePages.get(0);
    }
}
