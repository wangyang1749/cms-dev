package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.entity.Sheet;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.params.SheetParam;
import com.wangyang.cms.repository.SheetRepository;
import com.wangyang.cms.service.ISheetService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
@Slf4j
public class SheetServiceImpl extends BaseArticleServiceImpl<Sheet> implements ISheetService {

    @Autowired
    SheetRepository sheetRepository;

    @Autowired
    ITemplateService templateService;

    @Override
    public Sheet add(SheetParam sheetParam){
        Sheet sheet = new Sheet();
        BeanUtils.copyProperties(sheetParam,sheet);
        sheet = super.createOrUpdate(sheet);
        Sheet saveSheet = sheetRepository.save(sheet);
        convertSheet(sheet);
        return saveSheet;

    }

    @Override
    public void deleteAll() {
        sheetRepository.deleteAll();
    }

    private void convertSheet(Sheet sheet) {
        Template template = templateService.findById(sheet.getTemplateId());

        TemplateUtil.convertHtmlAndSave(sheet,template);
        log.info("Generate html in "+sheet.getPath()+"/"+sheet.getViewName());
    }

    @Override
    public Sheet findById(int id){
        Optional<Sheet> sheetOptional = sheetRepository.findById(id);
        if(!sheetOptional.isPresent()){
            throw new ObjectException("Sheet did't exist!!");
        }
        return sheetOptional.get();
    }

    @Override
    public Sheet update(int id, SheetParam updateSheet){
        Sheet sheet = findById(id);
        TemplateUtil.deleteTemplateHtml(sheet.getViewName(),sheet.getPath());
        BeanUtils.copyProperties(updateSheet,sheet);
        convertSheet(sheet);
        return sheetRepository.save(sheet);
    }

    @Override
    public void deleteById(int id){
        Sheet sheet = findById(id);
        TemplateUtil.deleteTemplateHtml(sheet.getViewName(),sheet.getPath());
        sheetRepository.deleteById(id);

    }

    @Override
    public ModelAndView preview(int id){
        Sheet sheet = findById(id);
        Template template = templateService.findById(sheet.getTemplateId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",sheet);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }

    @Override
    public String generateHtml(int id){
        Sheet sheet = findById(id);
        Template template = templateService.findById(sheet.getTemplateId());
        return TemplateUtil.convertHtmlAndSave(sheet,template);
    }

    @Override
    public Page<Sheet> list(Pageable pageable){
        return sheetRepository.findAll(pageable);
    }

}
