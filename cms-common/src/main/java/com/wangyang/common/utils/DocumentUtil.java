package com.wangyang.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocumentUtil {

    public static  String getDivContent(String html,String id){
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select(id);
        if(rows.size()<=0){
//            throw new DocumentException("文档中没有找到id"+id);
            return StringUtils.EMPTY;
        }

        Element row = rows.get(0);
        return row.html();
    }
}
