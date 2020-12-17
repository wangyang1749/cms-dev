package com.wangyang.common.utils;

import com.wangyang.common.CmsConst;

import java.util.Calendar;

public class CMSUtils {

    public static String randomViewName(){
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month =date.get(Calendar.MONTH);
        int second = date.get(Calendar.SECOND);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int millisecond = date.get(Calendar.MILLISECOND);
        String viewName = String.valueOf(year)+String.valueOf(month)+String.valueOf(day)+String.valueOf(second)+String.valueOf(millisecond);
//        return UUID.randomUUID().toString();
        return viewName;
    }

    public static String getArticlePath(){
        return CmsConst.ARTICLE_DETAIL_PATH;
    }

    public static String getCategoryPath(){
        return CmsConst.CATEGORY_LIST_PATH;
    }
    public static String getComponentsPath(){
        return CmsConst.COMPONENTS_PATH;
    }

    public static String getSheetPath(){
        return CmsConst.SHEET_PATH;
    }



    public static String randomTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public static String getHostAddress(){
        /**
        try {
            InetAddress address = InetAddress.getLocalHost();
            return String.format("http://%s:%s",address.getHostAddress(),"8080");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
         **/
        return String.format("http://%s:%s", "127.0.0.1", "8080");
    }


}
