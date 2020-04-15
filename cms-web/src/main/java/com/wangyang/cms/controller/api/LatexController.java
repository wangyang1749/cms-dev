package com.wangyang.cms.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.utils.LatexUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/latex")
public class LatexController {

    @RequestMapping(value = "/svg",produces = "image/svg+xml")
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
