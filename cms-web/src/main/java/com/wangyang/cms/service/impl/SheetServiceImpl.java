package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.dto.SheetDto;
import com.wangyang.cms.pojo.entity.Menu;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.vo.SheetVo;
import com.wangyang.cms.pojo.entity.Channel;
import com.wangyang.cms.pojo.entity.Sheet;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import com.wangyang.cms.repository.ChannelRepository;
import com.wangyang.cms.repository.MenuRepository;
import com.wangyang.cms.repository.SheetRepository;
import com.wangyang.cms.service.IChannelService;
import com.wangyang.cms.service.ISheetService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.CMSUtils;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    ChannelRepository channelRepository;
    @Autowired
    MenuRepository menuRepository;

    @Override
    public Sheet addOrUpdate(Sheet sheet){
//        Sheet sheet = new Sheet();
//        BeanUtils.copyProperties(sheetParam,sheet);
//        Channel channel = channelService.findById(sheet.getChannelId());
        //如何Channel中没有存储文章路径

        sheet = super.createOrUpdate(sheet);
        sheet.setPath("sheet");
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
        Sheet saveSheet = sheetRepository.save(sheet);
        return saveSheet;

    }

    @Override
    public Sheet save(Sheet sheet){
        return  sheetRepository.save(sheet);
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

    @Override
    public ModelAndView preview(int id){
        Sheet sheet = findById(id);

//        Template template = templateService.findById(sheetDetailVo.getChannel().getArticleTemplateId());
        Template template = templateService.findByEnName(sheet.getTemplateName());
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("view", sheet);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
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
            menu.setUrlName(sheet.getPath()+"/"+sheet.getViewName()+".html");
            menuRepository.save(menu);

        }
        return  sheetRepository.save(sheet);
    }
}
