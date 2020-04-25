package com.wangyang.syscall.http;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class CmsHttpClient {

    public static void main(String[] args) throws IOException {
        Content content = Request.Get("http://47.93.201.74")
                .execute().returnContent();
        System.out.println(content.asString());
    }
}
