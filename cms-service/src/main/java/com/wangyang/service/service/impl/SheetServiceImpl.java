package com.wangyang.service.service.impl;


import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.service.service.ISheetService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.entity.Sheet;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.vo.SheetVo;
import com.wangyang.service.repository.MenuRepository;
import com.wangyang.service.repository.SheetRepository;
import com.wangyang.pojo.entity.Menu;
import com.wangyang.common.CmsConst;
import com.wangyang.service.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SheetServiceImpl extends BaseArticleServiceImpl<Sheet> implements ISheetService {

    @Autowired
    SheetRepository sheetRepository;

    @Autowired
    ITemplateService templateService;
//    @Autowired
//    IChannelService channelService;

    @Autowired
    MenuRepository menuRepository;

    @Override
    public Sheet addOrUpdate(Sheet sheet){
//        Sheet sheet = new Sheet();
//        BeanUtils.copyProperties(sheetParam,sheet);
//        Channel channel = channelService.findById(sheet.getChannelId());
        //如何Channel中没有存储文章路径

        sheet = super.createOrUpdate(sheet);
        sheet.setPath(CMSUtils.getSheetPath());
//        sheet.setPath(channel.getPath()+"/"+channel.getName());
        if(sheet.getViewName()==null||"".equals(sheet.getViewName())){
            sheet.setViewName(CMSUtils.randomViewName());
        }
        if(sheet.getTemplateName()==null||"".equals(sheet.getTemplateName())){
            sheet.setTemplateName(CmsConst.DEFAULT_SHEET_TEMPLATE);
        }

//        if(channel.getFirstSheet()==null||"".equals(channel.getFirstSheet())){
//            channel.setFirstSheet(sheet.getViewName());
//            channel = channelService.save(channel);
//        }
        sheet.setStatus(ArticleStatus.PUBLISHED);
        Sheet saveSheet = sheetRepository.save(sheet);
        return saveSheet;

    }

    @Override
    public Sheet save(Sheet sheet){
        return  sheetRepository.save(sheet);
    }

    @Override
    public List<Sheet> listAll() {
        return sheetRepository.findAll();
    }
    //    @Override
//    public List<SheetDto> findListByChannelId(int channelId){
//        return  sheetRepository.findByChannelId(channelId).stream().map(sheet -> {
//            SheetDto sheetDto = new SheetDto();
//            BeanUtils.copyProperties(sheet,sheetDto);
//            return sheetDto;
//        }).collect(Collectors.toList());
//    }

    @Override
    public void deleteAll() {
        sheetRepository.deleteAll();
    }

//    private void convertSheet(Sheet sheet) {
//        Template template = templateService.findById(sheet.getTemplateId());
//
//        TemplateUtil.convertHtmlAndSave(sheet,template);
//        log.info("Generate html in "+sheet.getPath()+"/"+sheet.getViewName());
//    }

    @Override
    public Sheet findById(int id){
        Optional<Sheet> sheetOptional = sheetRepository.findById(id);
        if(!sheetOptional.isPresent()){
            throw new ObjectException("Sheet 没有找到!");
        }
        return sheetOptional.get();
    }

    @Override
    public Sheet update(Sheet sheet){
        sheet = super.createOrUpdate(sheet);
//        convertSheet(sheet);
        return sheetRepository.save(sheet);
    }

    @Override
    public Sheet deleteById(int id){
        Sheet sheet = findById(id);
        sheetRepository.deleteById(id);
        return sheet;
    }



//    @Override
//    public SheetDetailVo getSheetVoById(int id){
//        Sheet sheet = findById(id);
//        Channel channel = channelService.findById(sheet.getChannelId());
//        SheetDetailVo sheetDetailVo = new SheetDetailVo();
//        BeanUtils.copyProperties(sheet, sheetDetailVo);
//        sheetDetailVo.setChannel(channel);
//        return sheetDetailVo;
//    }


    @Override
    public Page<Sheet> list(Pageable pageable){
        return sheetRepository.findAll(pageable);
    }


    @Override
    public Page<SheetVo> conventTo(Page<Sheet> sheetPage){
        Page<SheetVo> sheetVoPage = sheetPage.map(sheet -> {
            SheetVo sheetVo = new SheetVo();
            BeanUtils.copyProperties(sheet, sheetVo);
            return sheetVo;
        });
        return sheetVoPage;
    }

    @Override
    public Sheet addOrRemoveToMenu(int id){
        Sheet sheet = findById(id);
        Menu menu = menuRepository.findBySheetId(sheet.getId());
        if(sheet.getExistNav()){
            sheet.setExistNav(false);
            if(menu!=null){
                menuRepository.deleteById(menu.getId());
            }
        }else{
            sheet.setExistNav(true);
            if(menu==null){
                menu = new Menu();
            }

            menu.setName(sheet.getTitle());
            menu.setSheetId(sheet.getId());
            menu.setUrlName(FormatUtil.sheetListFormat(sheet));
            menuRepository.save(menu);

        }
        return  sheetRepository.save(sheet);
    }
}
