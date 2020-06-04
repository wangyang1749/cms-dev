package com.wangyang;

import com.google.common.base.Joiner;
import com.wangyang.common.utils.MarkdownUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class TestMarkdown {

    @Test
    public void  test1() throws  Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TestMarkdown.class.getClassLoader().getResource("01.md").getPath())));
        List<String> list = br.lines().collect(Collectors.toList());
        String str = Joiner.on("\n").join(list);
        String[] html = MarkdownUtils.renderHtml(str);
        System.out.println(html[1]);
    }
}
