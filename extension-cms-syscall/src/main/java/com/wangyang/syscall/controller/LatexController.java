package com.wangyang.syscall.controller;

import com.alibaba.fastjson.JSONObject;

import com.wangyang.common.BaseResponse;
import com.wangyang.syscall.utils.LatexUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/latex")
public class LatexController {

    @RequestMapping(value = "/svg")
    public String getSvg(@RequestBody String latex ){
        if (latex==null){
            return "请输入公式,例如: ?math=\\sum_{i=1}^n a_i=0";
        }
        String inputLatex = JSONObject.parseObject(latex).getString("latex");
        if(inputLatex==null){
            return "请输入公式,例如: \\sum_{i=1}^n a_i=0";
        }
        return LatexUtil.getSvg(inputLatex,false);
    }

    @RequestMapping(value = "/svgSave")
    public BaseResponse getSvgPath(@RequestBody String latex ){
        if (latex==null){
            return BaseResponse.error("请输入公式,例如: ?math=\\sum_{i=1}^n a_i=0");
        }
        String inputLatex = JSONObject.parseObject(latex).getString("latex");
        if(inputLatex==null){
            return BaseResponse.error("请输入公式,例如: ?math=\\sum_{i=1}^n a_i=0");
        }
        return LatexUtil.getSvgPath(inputLatex);
    }

}
