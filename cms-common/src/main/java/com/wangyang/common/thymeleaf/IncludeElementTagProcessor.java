package com.wangyang.common.thymeleaf;

import com.google.common.base.Joiner;
import com.wangyang.common.utils.FileUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressionParser;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2020/12/15
 */
public class IncludeElementTagProcessor extends AbstractElementTagProcessor {
    private static final String TAG_NAME  = "include";//标签名 select 这个玩意就是 自定义标签的 ： select， 应该是可以定义多个标签
    private static final int PRECEDENCE = 1000;//优先级
    private String workDir;
    public IncludeElementTagProcessor(String dialectPrefix,String workDir) {
        super(TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix, // 要应用于名称的匹配前缀
                TAG_NAME, // 标签名称：匹配此名称的特定标签
                false, // 没有应用于标签名的前缀
                null, // 无属性名称：将通过标签名称匹配
                false, // 没有要应用于属性名称的前缀
                PRECEDENCE// 优先(内部方言自己的优先
        );
        this.workDir = workDir;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        String filePath = tag.getAttributeValue("href");
        IModelFactory modelFactory = context.getModelFactory();
        IModel model = modelFactory.createModel();
//        model.add(modelFactory.createText("\n\t"));
        //5、添加模型元素
//        IOpenElementTag openElementTag = addModelElement(tag,modelFactory);
//        model.add(openElementTag);
        //model.add(modelFactory.createText("\n\t\t"));
//        model.add(modelFactory.createText(HtmlEscape.unescapeHtml(String.join("\n\t", options))));
        //model.add(modelFactory.createText("\n\t"));
//        model.add(modelFactory.createCloseElementTag("select"));
        //6、替换前面的标签


        if(workDir!=null){
            model.add(modelFactory.createText("\n\t"));
            File file = new File(workDir+filePath);
            // Joiner.on("\n").join(Files.lines(file.toPath()).collect(Collectors.toList()));
            String html;
            if(file.exists()){
                html = FileUtils.convert(file);
            }else {
                html ="include file is not exist!";
            }
            model.add(modelFactory.createText(html));
            structureHandler.replaceWith(model, false);
        }else {
            model.add(modelFactory.createText("\n\t"));
            model.add(modelFactory.createText("<!--#include file='"+filePath+"'-->"));
            structureHandler.insertImmediatelyAfter(model, false);
        }
    }

    /**
     * 执行自定义标签中的表达式
     * @param value
     * @param context
     * @return
     */
    private Object executeExpression(String value, ITemplateContext context) {
        StandardExpressionParser parser = new StandardExpressionParser();
        Expression parseExpression = parser.parseExpression(context, value);
        Object execute = parseExpression.execute(context);
        return execute;
    }

}
