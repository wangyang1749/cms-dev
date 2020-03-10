package com.wangyang.cms.util;

import com.wangyang.cms.utils.NodeJsUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestChrome {

//    @Test
    @Ignore
    public void test(){
        long time = System.currentTimeMillis();
        // 可省略，若驱动放在其他目录需指定驱动路径
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.get("http://baidu.com");
        // 休眠1s,为了让js执行完
//        Thread.sleep(1000l);
        // 网页源码
        String source = driver.getPageSource();
        System.out.println(source);
        driver.close();
        System.out.println("耗时:"+(System.currentTimeMillis()-time));

    }


//    @Test
    @Ignore
    public void test2() throws Exception{
        Runtime rt = Runtime.getRuntime();
//        String exec =  nodeJSPath + BLANK + chromePath + BLANK + url;
//        Process p = rt.exec("google-chrome --headless --disable-gpu --print-to-pdf='/home/wy/Documents/cms/temp/output.pdf'  /home/wy/Documents/cms/temp/test.html");//这里我的codes.js是保存在c盘下面的phantomjs目录
        Process p = rt.exec("node /home/wy/Documents/cms/cms-web/src/main/resources/01.js");
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while((tmp = br.readLine())!=null){
            sbf.append(tmp);
        }
//        System.out.println(sbf.toString());
        System.out.println(sbf);
    }

//    @Test
    @Ignore
    public void test3(){
        String result = NodeJsUtil.execNodeJs("node", "/home/wy/Documents/cms/cms-web/src/main/resources/01.js");
        System.out.println(result);
    }
}
