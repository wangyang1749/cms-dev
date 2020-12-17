package com.wangyang.service.service;

import com.wangyang.pojo.entity.Sheet;
import com.wangyang.pojo.vo.SheetVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISheetService extends IBaseArticleService<Sheet>{

    List<Sheet> listAll();
    Sheet save(Sheet sheet);

//    List<SheetDto> findListByChannelId(int channelId);

    void deleteAll();

    Sheet addOrUpdate(Sheet sheet);

    Sheet findById(int id);

    Sheet update(Sheet updateSheet);

    Sheet deleteById(int id);



//    SheetDetailVo getSheetVoById(int id);

//    String generateHtml(int id);

    Page<Sheet> list(Pageable pageable);

    Page<SheetVo> conventTo(Page<Sheet> sheetPage);

    Sheet addOrRemoveToMenu(int id);

//    Page<SheetVo> conventTo(Page<Sheet> sheetPage);
}
