package com.wangyang.cms.util;

import com.google.common.base.Joiner;
import com.wangyang.cms.utils.MarkdownUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class TestMarkDown {

    @Test
    public void test() throws Exception{
//        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test.md");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
//
//        List<String> list = reader.lines().collect(Collectors.toList());
//        String content = Joiner.on("\n").join(list);
//        String[] html = MarkdownUtils.renderHtml(content);
//        System.out.println(html[1]);
    }
}
