package com.wangyang.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

@Controller
public class TestController {


    @RequestMapping("/hello7")
    public String hello7(Model model){
        model.addAttribute("name","zhangsan");
        return "templates/test";
    }
    @RequestMapping("/hello8")
    public String hello8(Model model){
        model.addAttribute("name","zhangsan");
        return "templates/test1";
    }


    @RequestMapping("/hello6")
//    @ResponseBody
    public String hello6(Model model){
        model.addAttribute("name","zhangsan");
        String template = "<div th:text=\"${name}\">7896523</div>";
        Context context = new Context();
        context.setVariable("name","zhangsan");
        String html = "";//TemplateUtil.thymeleaf2HtmlByString(template, context);
        return template;
    }
    /**
     *
     * @return
     */
    @RequestMapping("/hello")
    public String hello(){
        return "@cms";
    }

    @RequestMapping("/hello2")
    @ResponseBody
    public List<String> hello2(){
        return Arrays.asList("1","2");
    }

    @RequestMapping("/hello3")
    public ModelAndView  hello3(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","zhangsan");
        modelAndView.setViewName("@cms");
        return modelAndView;
    }
    @RequestMapping("/hello4")
    public ModelAndView hello4(){
//        System.out.println("准备下载学生列表");
//        User user = new User();
//        user.setUsername("zhangsan");
//        user.setPassword("123456");
//        ArrayList<User> list = new ArrayList<>();
//        list.add(user);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("list", list);
//        View view = new ExcelView();
//        mv.setView(view); //注意: 这里是将实例化的自定义视图对象当做参数传进入, 而不是视图名字
//        return mv;
        return null;
    }
    @RequestMapping("/hello5")
    public ModelAndView hello5(){
//        System.out.println("准备下载学生列表");
//        User user = new User();
//        user.setUsername("zhangsan");
//        user.setPassword("123456");
//        ArrayList<User> list = new ArrayList<>();
//        list.add(user);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("list", list);
////        View view = new PdfView();
////        mv.setView(view); //注意: 这里是将实例化的自定义视图对象当做参数传进入, 而不是视图名字
        return null;
    }




}
