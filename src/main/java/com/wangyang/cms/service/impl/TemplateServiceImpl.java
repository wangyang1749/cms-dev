package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.repository.TemplateRepository;
import com.wangyang.cms.service.ITemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    TemplateRepository templateRepository;
    @Override
    public Template add(Template template) {
        return templateRepository.save(template);
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
    public List<Template> findByTemplateType(TemplateType type) {
        return templateRepository.findByTemplateType(type);
    }
}
