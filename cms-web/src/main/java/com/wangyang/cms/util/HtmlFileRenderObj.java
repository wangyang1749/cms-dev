package com.wangyang.cms.util;

import com.wangyang.model.pojo.entity.Template;

public interface HtmlFileRenderObj<T> extends HtmlFileRender{
    T data();
    Template template(T t);
}
