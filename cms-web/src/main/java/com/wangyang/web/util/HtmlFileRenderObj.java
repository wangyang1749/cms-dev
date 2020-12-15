package com.wangyang.web.util;

import com.wangyang.pojo.entity.Template;

public interface HtmlFileRenderObj<T> extends HtmlFileRender{
    T data();
    Template template(T t);
}
