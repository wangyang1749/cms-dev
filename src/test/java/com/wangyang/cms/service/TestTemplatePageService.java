package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.repository.TemplatePageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class TestTemplatePageService {

    @Autowired
    TemplatePageRepository templatePageRepository;
    @Autowired
    IComponentsService templatePageService;

    @Test
    public void test1(){
        Components templatePage = new Components();
        templatePage.setStatus(true);
        List<Components> templatePages = templatePageRepository.findAll(new Specification<Components>() {
            @Override
            public Predicate toPredicate(Root<Components> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                Predicate[] predicate={
                        criteriaBuilder.isTrue(root.get("status")),
                };

                criteriaQuery.where(criteriaBuilder.and(predicate));
                return criteriaQuery.getRestriction();
            }
        });
        System.out.println(templatePages.size());
    }

    @Test
    public void test2(){
        Components dataName = templatePageService.findByDataName("categoryServiceImpl.listAsTree");
        System.out.println(dataName);
    }

    @Test
    public void test3(){
        List<Components> templatePages = templatePageRepository.findAll((Specification<Components>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.like(root.get("event"), "%AC%"),
                        criteriaBuilder.isTrue(root.get("status"))
                ).getRestriction());

        System.out.println(templatePages);
    }
}
