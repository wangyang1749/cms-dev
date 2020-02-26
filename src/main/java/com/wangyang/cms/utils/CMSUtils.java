package com.wangyang.cms.utils;

import java.util.UUID;

public class CMSUtils {

    public static String randomViewName(){
        return UUID.randomUUID().toString();
    }
}
