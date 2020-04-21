package com.wangyang.cms.core.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashSet;
import java.util.Set;

@Component
public class CmsDialect  extends AbstractProcessorDialect {
    //定义方言名称
    private static final String DIALECT_NAME = "Cms Dialect";

    public CmsDialect() {
        //设置自定义方言与"方言处理器"优先级相同
        super(DIALECT_NAME, "cms", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new CustomLabel(dialectPrefix));
        return processors;
    }
}
