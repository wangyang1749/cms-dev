package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.expection.OptionException;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.repository.TemplateRepository;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.ITemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    ICategoryService categoryService;

    @Override
    public Template add(Template template) {
        return templateRepository.save(template);
    }


    @Override
    public Optional<Template> findOptionalById(int id){
        return templateRepository.findById(id);
    }
    @Override
    public List<Template> saveAll(List<Template> templates) {
        return templateRepository.saveAll(templates);
    }

    @Override
    public Template update(int id,Template updateTemplate) {
        Template template = findById(id);
        BeanUtils.copyProperties(updateTemplate,template,"id");
        return templateRepository.save(template);
    }

    @Override
    public List<Template> findAll(){
        return templateRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        templateRepository.deleteById(id);
    }

    @Override
    public Template findById(int id) {
        Optional<Template> templateOptional = templateRepository.findById(id);
        if(!templateOptional.isPresent()){
            throw  new ObjectException("Not found template");
        }
        return templateOptional.get();
    }

    @Override
    public Page<Template> list(Pageable pageable) {
        return templateRepository.findAll(pageable);
    }


    @Override
    public List<Template> findByTemplateType(TemplateType templateType) {
        Specification<Template> specification = new Specification<Template>() {
            @Override
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("templateType"),templateType)).getRestriction();
            }
        };
        return templateRepository.findAll(specification, Sort.by(Sort.Order.asc("order")));

    }


    @Override
    public List<Template> listByAndStatusTrue(TemplateType templateType){
        Specification<Template> specification = new Specification<Template>() {
            @Override
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.isTrue(root.get("status")),
                                        criteriaBuilder.equal(root.get("templateType"),templateType)).getRestriction();
            }
        };
        return templateRepository.findAll(specification, Sort.by(Sort.Order.asc("order")));
    }

    @Override
    public Template findByEnName(String enName){
        Template template = templateRepository.findByEnName(enName);
        if(template==null){
            throw new ObjectException(enName+"Template模板没有找到!!!");
        }
        return template;
    }

    @Override
    public Optional<Template> findOptionalByEnName(String enName){
        Template template = templateRepository.findByEnName(enName);
        return Optional.ofNullable(template);
    }

    @Override
    public void deleteAll() {
        templateRepository.deleteAll();
    }



    @Override
    public Template setStatus(int id){
        Template template = findById(id);
        if(template.getStatus()){
            template.setStatus(false);
        }else {
            List<CategoryDto> categoryDtos = categoryService.listBy(template.getEnName());
            if(categoryDtos.size()==0){
                throw new OptionException("不能启用"+template.getName()+"在首页,因为该模板下没有分类!");
            }
            template.setStatus(true);
        }
        return templateRepository.save(template);
    }
}
