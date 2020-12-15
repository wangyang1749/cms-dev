package com.wangyang.web.util;

import com.wangyang.pojo.entity.Template;

import java.util.Map;

public interface HtmlFileRenderMap extends HtmlFileRender {
    Map<String,Object> data();
    Template template();
}
