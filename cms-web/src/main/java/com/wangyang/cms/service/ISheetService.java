package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Sheet;
import com.wangyang.cms.pojo.params.SheetParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

public interface ISheetService {

    void deleteAll();

    Sheet add(SheetParam sheet);

    Sheet findById(int id);

    Sheet update(int id, SheetParam updateSheet);

    void deleteById(int id);

    ModelAndView preview(int id);

    String generateHtml(int id);

    Page<Sheet> list(Pageable pageable);
}
