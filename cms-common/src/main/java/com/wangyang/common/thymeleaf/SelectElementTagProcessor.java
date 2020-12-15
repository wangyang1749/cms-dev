package com.wangyang.common.thymeleaf;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author wangyang
 * @date 2020/12/15
 */
public class SelectElementTagProcessor extends AbstractElementTagProcessor {
    private static final String TAG_NAME  = "select";//标签名 select 这个玩意就是 自定义标签的 ： select， 应该是可以定义多个标签
    private static final int PRECEDENCE = 1000;//优先级

    public SelectElementTagProcessor(String dialectPrefix) {
//        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
        super(TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix, // 要应用于名称的匹配前缀
                TAG_NAME, // 标签名称：匹配此名称的特定标签
                true, // 没有应用于标签名的前缀
                null, // 无属性名称：将通过标签名称匹配
                false, // 没有要应用于属性名称的前缀
                PRECEDENCE// 优先(内部方言自己的优先
        );
    }


    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //1、select的option
        List<String> options=new ArrayList<>();
        //2、根据字典类型获取数据,有过value有值，则默认选中
        String ptionHtml = getDictDateByType(tag,context);
        options.add(ptionHtml);
        //3、设置默认值
        String defaultHtml = addDefault(tag);
        options.add(0,defaultHtml);
        //4、创建模型
        IModelFactory modelFactory = context.getModelFactory();
        IModel model = modelFactory.createModel();
        //model.add(modelFactory.createText("\n\t"));
        //5、添加模型元素
        IOpenElementTag openElementTag = addModelElement(tag,modelFactory);
        model.add(openElementTag);
        //model.add(modelFactory.createText("\n\t\t"));
        model.add(modelFactory.createText(HtmlEscape.unescapeHtml(String.join("\n\t", options))));
        //model.add(modelFactory.createText("\n\t"));
        model.add(modelFactory.createCloseElementTag("select"));
        //6、替换前面的标签
        structureHandler.replaceWith(model, false);

    }

    /**
     * 添加默认值
     * @param tag
     * @return
     */
    private String addDefault(IProcessableElementTag tag) {
        String headerLabel = tag.getAttributeValue("headerLabel");
        String headerValue = tag.getAttributeValue("headerValue");
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(headerLabel)) {
            sb.append("<option value='" + headerValue + "' >");
            sb.append(headerLabel + "</option>");
        }
        return sb.toString();
    }

    /**
     * 根据字典类型获取数据
     * @param tag
     * @param context
     * @return
     */
    private String getDictDateByType(IProcessableElementTag tag, ITemplateContext context) {
        String dictType = tag.getAttributeValue("dictType");
        if (StringUtils.isEmpty(dictType)) {
            return "";
        }
        String value = tag.getAttributeValue("th:value");
        Object executeExpression = null;
        if (!StringUtils.isEmpty(value)) {
            executeExpression = executeExpression(value, context);// 执行表达式
        }

        List<Map<String, Object>> dicts= null;//= DictUtil.getDictByType(dictType);
        if (CollectionUtils.isEmpty(dicts)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = dicts.size(); i < len; i++) {
            Map<String, Object> map = dicts.get(i);
            if (executeExpression != null && executeExpression.toString().equals(map.get("value"))) {
                sb.append("<option value='" + map.get("value") + "' selected=\"selected\">");
            } else {
                sb.append("<option value='" + map.get("value") + "' >");
            }
            sb.append(map.get("label") + "</option>");
        }
        return sb.toString();
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

    /**
     * 添加模型元素
     * @param tag
     * @param modelFactory
     * @return
     */
    private IOpenElementTag addModelElement(IProcessableElementTag tag, IModelFactory modelFactory) {
        String classValue = tag.getAttributeValue("class");
        String id = tag.getAttributeValue("id");
        String name = tag.getAttributeValue("name");
        String style = tag.getAttributeValue("style");
        IOpenElementTag openElementTag = modelFactory.createOpenElementTag("select", "class", classValue);
        if (!StringUtils.isEmpty(id)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "id", id);
        }
        if (!StringUtils.isEmpty(name)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "name", name);
        }
        if (!StringUtils.isEmpty(style)) {
            openElementTag = modelFactory.setAttribute(openElementTag, "style", style);
        }
        return openElementTag;
    }

}
