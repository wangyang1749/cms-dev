package com.wangyang.web.util;

import com.wangyang.common.CmsConst;

public interface HtmlFileRender {
    default boolean isDebug(){
        return CmsConst.IS_DEBUG;
    }
    String path();
    String viewName();

}
