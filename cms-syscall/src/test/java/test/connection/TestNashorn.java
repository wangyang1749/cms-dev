package test.connection;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestNashorn {

    @Test
    public void test() throws Exception{
//        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test2.js");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
//        List<String> list = reader.lines().collect(Collectors.toList());
//        String content = Joiner.on("\n").join(list);
//
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
////        engine.eval("print('hello');"); // execute js from source
//        engine.eval(content); // execute js from file
//        Object eval = engine.eval("function getHtml(){var html = katex.renderToString('c = \\\\pm\\\\sqrt{a^2 + b^2}');return html}");
//        Invocable in = (Invocable) engine;
//        Object getHtml = ((Invocable) engine).invokeFunction("getHtml");
//        System.out.println(getHtml);
////        Invocable invocable = (Invocable) engine;
//        Object sum = invocable.invokeFunction("katex", "c = \\\\pm\\\\sqrt{a^2 + b^2}");
//        System.out.println(sum);
//        invocable.invokeFunction("f2", new Date());
//        invocable.invokeFunction("f2", LocalDateTime.now());
    }

    @Test
    public void test2() throws  Exception{
//        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("mermaid.min.js");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
//        List<String> list = reader.lines().collect(Collectors.toList());
//        String content = Joiner.on("\n").join(list);
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        Object eval = engine.eval(getContent("05.js"));
//        Invocable in = (Invocable) engine;
//        Object getHtml = ((Invocable) engine).invokeFunction("getHtml");
//        System.out.println(getHtml);

    }


    public String getContent(String name) throws Exception{
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        List<String> list = reader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(list);
        return content;
    }


}
