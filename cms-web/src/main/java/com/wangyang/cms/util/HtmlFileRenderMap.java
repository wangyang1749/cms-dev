package com.wangyang.cms.util;

import com.wangyang.model.pojo.entity.Template;

import java.util.Map;

public interface HtmlFileRenderMap extends HtmlFileRender {
    Map<String,Object> data();
    Template template();
}
