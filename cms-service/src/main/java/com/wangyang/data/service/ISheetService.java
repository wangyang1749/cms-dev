package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Sheet;
import com.wangyang.model.pojo.vo.SheetVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISheetService extends IBaseArticleService<Sheet>{


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
