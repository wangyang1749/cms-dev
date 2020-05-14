package com.wangyang.weixin.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

public class CheckUtil{

    private static  final String token = "wangyang";
    public static boolean checkSignature(String signature,String timestamp,String  nonce){
        String[] arr = new String[]{token,timestamp,nonce};
        Arrays.sort(arr);
        StringBuffer content = new StringBuffer();
        for (int i = 0;i<arr.length;i++){
            content.append(arr[i]);
        }
        String shaEncode = shaEncode(content.toString());
        return shaEncode.equals(signature);

    }

    public static String shaEncode(String inStr)  {
        StringBuffer hexValue = null;
        try {
            MessageDigest sha = null;
            try {
                sha = MessageDigest.getInstance("SHA");
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
                return "";
            }

            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = sha.digest(byteArray);
            hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hexValue.toString();
    }

}

