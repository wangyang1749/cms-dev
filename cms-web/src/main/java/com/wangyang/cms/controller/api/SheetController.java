package com.wangyang.cms.controller.api;

import com.wangyang.data.service.IHtmlService;
import com.wangyang.data.service.ISheetService;
import com.wangyang.model.pojo.entity.Sheet;
import com.wangyang.model.pojo.vo.SheetVo;
import com.wangyang.model.pojo.params.SheetParam;
import com.wangyang.common.utils.TemplateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/sheet")
public class SheetController {

    @Autowired
    ISheetService sheetService;

    @Autowired
    IHtmlService htmlService;

    @PostMapping
    public Sheet add(@RequestBody SheetParam sheetParam){
        Sheet sheet = new Sheet();
        BeanUtils.copyProperties(sheetParam,sheet);
        Sheet saveSheet = sheetService.addOrUpdate(sheet);
        htmlService.convertArticleListBy(saveSheet);
        return saveSheet;
    }

    @PostMapping("/update/{id}")
    public Sheet update(@PathVariable("id") int id,@RequestBody SheetParam sheetParam){
        Sheet sheet = findById(id);
        BeanUtils.copyProperties(sheetParam,sheet);
        Sheet updateSheet = sheetService.addOrUpdate(sheet);
        htmlService.convertArticleListBy(updateSheet);
        return updateSheet;
    }

    @GetMapping
    public Page<SheetVo> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Sheet> sheetPage = sheetService.list(pageable);
        return sheetService.conventTo(sheetPage);
    }
    @GetMapping("/find/{id}")
    public Sheet findById(@PathVariable("id") Integer id){
        return sheetService.findById(id);
    }

    @GetMapping("/generate/{id}")
    public Sheet generate(@PathVariable("id") Integer id){
        Sheet sheet = sheetService.findById(id);
        htmlService.convertArticleListBy(sheet);
        return sheet;
    }
    @RequestMapping("/delete/{id}")
    public Sheet deleteById(@PathVariable("id") Integer id){
        Sheet sheet = sheetService.deleteById(id);
        TemplateUtil.deleteTemplateHtml(sheet.getViewName(),sheet.getPath());
        return sheet;
    }

    @GetMapping("/addOrRemoveToMenu/{id}")
    public Sheet addOrRemoveToMenu(@PathVariable("id") int id){
        Sheet sheet = sheetService.addOrRemoveToMenu(id);
        htmlService.generateMenuListHtml();
        return sheet;
    }

//    @GetMapping("/findListByChannelId/{id}")
//    public List<SheetDto> findListByChannelId(@PathVariable("id") Integer id){
//        return sheetService.findListByChannelId(id);
//    }
}
