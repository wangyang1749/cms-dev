package com.wangyang.web.util;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.DocumentUtil;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;

import java.io.File;

public class HtmlFileRenderHandler {
    public static <T> String render(HtmlFileRenderObj<T> htmlFileRender){
        if(htmlFileRender.isDebug()){
            T t = htmlFileRender.data();
            String html = FileUtils.convertByString(TemplateUtil.convertHtmlAndPreview(t,htmlFileRender.template(t)));
            return DocumentUtil.addDebugLabel(html);
        }
        File file= new File(CmsConst.WORK_DIR+ "/html/" +htmlFileRender.path()+"/"+htmlFileRender.viewName()+".html");
        String result;
        if(file.exists()){
            result = FileUtils.convert(file);
        }else {
            T t = htmlFileRender.data();
            String htmlAndSave = TemplateUtil.convertHtmlAndSave(t, htmlFileRender.template(t));
            result = FileUtils.convertByString(htmlAndSave);
        }
        return result;
    }

    public static  String render(HtmlFileRenderMap htmlFileRender){
        if(htmlFileRender.isDebug()){
             String html = FileUtils.convertByString(TemplateUtil.convertHtmlAndPreview(htmlFileRender.data(),htmlFileRender.template()));
             return DocumentUtil.addDebugLabel(html);
        }
        File file= new File(CmsConst.WORK_DIR+ "/html/" +htmlFileRender.path()+"/"+htmlFileRender.viewName()+".html");
        String result;
        if(file.exists()){
            result = FileUtils.convert(file);
        }else {
            String htmlAndSave = TemplateUtil.convertHtmlAndSave(htmlFileRender.path(), htmlFileRender.viewName(), htmlFileRender.data(), htmlFileRender.template());
            result = FileUtils.convertByString(htmlAndSave);
        }
        return result;
    }
}
