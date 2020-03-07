package com.wangyang.cms.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestCmd {

    @Test
    public void test() throws Exception{
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("node  /home/wy/Documents/cms/src/test/resources/03.js  c=\\pm\\sqrt{a^2+b^2}");//这里我的codes.js是保存在c盘下面的phantomjs目录

        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while((tmp = br.readLine())!=null){
            sbf.append(tmp);
        }
        //System.out.println(sbf.toString());
        System.out.println(sbf);
    }
}
