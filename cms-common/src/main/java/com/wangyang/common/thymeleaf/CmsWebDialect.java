package com.wangyang.common.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardIfTagProcessor;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.standard.processor.StandardTextTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

public class CmsDialect  extends AbstractProcessorDialect {
    //定义方言名称
    private static final String DIALECT_NAME = "Score Dialect";
    public CmsDialect() {
        //设置自定义方言与"方言处理器"优先级相同
        super(DIALECT_NAME, "cms", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new StandardReplaceTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardIfTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new IncludeElementTagProcessor(dialectPrefix));
        return processors;
    }
}
