package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Sheet;
import com.wangyang.cms.pojo.params.SheetParam;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.service.ISheetService;
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

    @PostMapping
    public Sheet add(@RequestBody SheetParam sheet){
        return sheetService.add(sheet);
    }

    @PostMapping("/update/{id}")
    public Sheet update(@PathVariable("id") int id,@RequestBody SheetParam sheet){
        return  sheetService.update(id,sheet);
    }

    @GetMapping
    public Page<Sheet> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return sheetService.list(pageable);
    }
    @GetMapping("/find/{id}")
    public Sheet findById(@PathVariable("id") Integer id){
        return sheetService.findById(id);
    }

    @GetMapping("/generate/{id}")
    public BaseResponse generate(@PathVariable("id") Integer id){
        return BaseResponse.ok(sheetService.generateHtml(id));
    }
    @RequestMapping("/delete/{id}")
    public BaseResponse deleteById(@PathVariable("id") Integer id){
        sheetService.deleteById(id);
        return BaseResponse.ok("delete success!");
    }
}
