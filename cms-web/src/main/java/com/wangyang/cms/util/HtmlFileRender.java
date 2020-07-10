package com.wangyang.cms.util;

import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.entity.Template;

import java.util.Map;

public interface HtmlFileRender {
    default boolean isDebug(){
        return CmsConst.IS_DEBUG;
    }
    String path();
    String viewName();

}
