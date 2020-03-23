package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.SheetDto;
import com.wangyang.cms.pojo.vo.SheetVo;
import com.wangyang.cms.pojo.entity.Sheet;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ISheetService {


    Sheet save(Sheet sheet);

//    List<SheetDto> findListByChannelId(int channelId);

    void deleteAll();

    Sheet addOrUpdate(Sheet sheet);

    Sheet findById(int id);

    Sheet update(Sheet updateSheet);

    Sheet deleteById(int id);

    ModelAndView preview(int id);

//    SheetDetailVo getSheetVoById(int id);

//    String generateHtml(int id);

    Page<Sheet> list(Pageable pageable);

    Page<SheetVo> conventTo(Page<Sheet> sheetPage);

    Sheet addOrRemoveToMenu(int id);

//    Page<SheetVo> conventTo(Page<Sheet> sheetPage);
}
