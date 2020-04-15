package com.wangyang.cms.controller.user;

import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.utils.LatexUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/latex")
public class LatexUserController {

    @ResponseBody
    @RequestMapping(value = "/svg",produces = "image/svg+xml")
    public String getSvg(@RequestParam(value = "latex",required = false) String latex ){
        if (latex==null){
            return "请输入公式,例如: ?math=\\sum_{i=1}^n a_i=0";
        }


        return LatexUtil.getSvg(latex,false);
    }

    @ResponseBody
    @RequestMapping(value = "/svgPath")
    public BaseResponse getSvgPath(@RequestParam(value = "latex",required = false) String latex ){
        if (latex==null){
            return BaseResponse.error("请输入公式,例如: ?math=\\sum_{i=1}^n a_i=0");
        }

        return LatexUtil.getSvgPath(latex);
    }

}
