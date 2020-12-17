package com.wangyang.common.thymeleaf;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.Fragment;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyang
 * @date 2020/12/17
 */
public class CmsFileReplaceTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME  = "replace";
    private static final int PRECEDENCE = 10000;
    private static String varPattern2 = "\\~\\{(.*)}";
    private static Pattern rv = Pattern.compile(varPattern2);
    public CmsFileReplaceTagProcessor(TemplateMode templateMode, String dialectPrefix) {
        super(
                templateMode, // This processor will apply only to HTML mode
                dialectPrefix,     // Prefix to be applied to name for matching
                null,              // No tag name: match any tag name
                false,             // No prefix to be applied to tag name
                ATTR_NAME,         // Name of the attribute that will be matched
                true,              // Apply dialect prefix to attribute name
                PRECEDENCE,        // Precedence (inside dialect's precedence)
                true);             // Remove the matched attribute afterwards
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {

        if(tag.hasAttribute("parser")){
            final IEngineConfiguration configuration = context.getConfiguration();
            final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
            if(attributeValue.startsWith("~")){
                Matcher matcher = rv.matcher(attributeValue);
                while (matcher.find()){
                    String attr = matcher.group(1);
                    final IStandardExpression expression = parser.parseExpression(context, attr);
                    Object execute = expression.execute(context);
                    String path = "~{"+execute.toString()+"} ?:_";
                    structureHandler.setAttribute("cms:replace",path);
                }
            }else {
                final IStandardExpression expression = parser.parseExpression(context, attributeValue);
                Object execute = expression.execute(context);
                structureHandler.setAttribute("cms:replace",execute.toString());
            }

       }else {
           structureHandler.setAttribute("cms:replace",attributeValue);
       }
    }
}
