package com.wangyang.syscall.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NodeJsUtil {

    public static String execNodeJs(String... params){
        if(params.length>0){
            String[] cmd = new String[params.length];
            for (int i=0;i<params.length;i++){
                cmd[i]=params[i];
            }
            return exec(cmd);
        }
        return null;
    }

    public static String exec(String[] command){
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(command);
//            process.waitFor();
            InputStream is = process.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            br.lines().forEach(line->{
                sb.append(line);
            });
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
