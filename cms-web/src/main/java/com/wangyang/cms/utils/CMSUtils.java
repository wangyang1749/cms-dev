package com.wangyang.cms.utils;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

public class CMSUtils {

    public static String randomViewName(){
        return UUID.randomUUID().toString();
    }

    public static String randomTime(){
        return new Date().toString();
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
