package com.wangyang.cms.util;

import com.wangyang.cms.utils.LatexUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestLatexUtil {

    @Test
    public void test(){
        String svgPath = LatexUtil.getSvg("\\sum_{i=1}^n=a_i=0",false);
        System.out.println(svgPath);
    }
}
