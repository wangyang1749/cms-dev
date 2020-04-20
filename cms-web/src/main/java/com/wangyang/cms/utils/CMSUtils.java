package com.wangyang.cms.utils;

import com.wangyang.cms.pojo.entity.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

    public static PageRequest articleListPageRequest(int page,Category category){
        Sort sort;
        if(category.getDesc()){
             sort= Sort.by(Sort.Direction.DESC, "order","id");
        }else {
            sort= Sort.by(Sort.Direction.ASC, "order","id");
        }

        PageRequest pageRequest = PageRequest.of(page,category.getArticleListSize(), sort);
        return pageRequest;
    }
}
