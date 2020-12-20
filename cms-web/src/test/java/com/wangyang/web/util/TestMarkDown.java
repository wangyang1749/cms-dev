package com.wangyang.web.util;

import org.junit.jupiter.api.Test;

public class TestMarkDown {

//    @Test
    public void test() throws Exception{
//        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test.md");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
//
//        List<String> list = reader.lines().collect(Collectors.toList());
//        String content = Joiner.on("\n").join(list);
//        String[] html = MarkdownUtils.renderHtml(content);
//        System.out.println(html[1]);
    }

    class A{

        private Integer age;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
    @Test
    public void test2(){
        System.out.println(new A().getAge()!=null);
    }

}
