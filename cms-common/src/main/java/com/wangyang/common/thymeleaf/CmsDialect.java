package com.wangyang.common.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashSet;
import java.util.Set;

public class CmsDialect  extends AbstractProcessorDialect {
    //定义方言名称
    private static final String DIALECT_NAME = "Score Dialect";
    private   String workDir;

    /**
     *
     * @param workDir 完整的模板路径，用includes header eg./root/cms/templates/header.html
     */
    public CmsDialect(String workDir) {
        //设置自定义方言与"方言处理器"优先级相同
        super(DIALECT_NAME, "score", StandardDialect.PROCESSOR_PRECEDENCE);
        this.workDir=workDir;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new CustomLabel(dialectPrefix));
        processors.add(new MatchDayTodayModelProcessor(dialectPrefix));
        processors.add(new SelectElementTagProcessor(dialectPrefix));
        processors.add(new IncludeElementTagProcessor(dialectPrefix,workDir));
        return processors;
    }
}
