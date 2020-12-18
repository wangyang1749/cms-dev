package com.wangyang.common.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.*;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CmsWebDialect extends AbstractProcessorDialect {
    //定义方言名称
    private static final String DIALECT_NAME = "Score Dialect";
    public CmsWebDialect() {
        //设置自定义方言与"方言处理器"优先级相同
        super(DIALECT_NAME, "cms", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
//        processors.add(new StandardTextTagProcessor(TemplateMode.HTML, dialectPrefix));
//        processors.add(new StandardValueTagProcessor(dialectPrefix));
//        processors.add(new StandardReplaceTagProcessor(TemplateMode.HTML, dialectPrefix));
//        processors.add(new StandardIfTagProcessor(TemplateMode.HTML, dialectPrefix));
//        processors.add(new StandardFragmentTagProcessor(TemplateMode.HTML, dialectPrefix));
//        processors.add(new StandardInlineHTMLTagProcessor(dialectPrefix));

//        processors.add(new IncludeElementTagProcessor(dialectPrefix));

        /*
         * It is important that we create new instances here because, if there are
         * several dialects in the TemplateEngine that extend StandardDialect, they should
         * not be returning the exact same instances for their processors in order
         * to allow specific instances to be directly linked with their owner dialect.
         */




        /*
         * ------------------------
         * ------------------------
         * HTML TEMPLATE MODE
         * ------------------------
         * ------------------------
         */


        /*
         * HTML: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardActionTagProcessor(dialectPrefix));
        processors.add(new StandardAltTitleTagProcessor(dialectPrefix));
        processors.add(new StandardAssertTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardAttrTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardAttrappendTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardAttrprependTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardCaseTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardClassappendTagProcessor(dialectPrefix));
        for (final String attrName : StandardConditionalFixedValueTagProcessor.ATTR_NAMES) {
            processors.add(new StandardConditionalFixedValueTagProcessor(dialectPrefix, attrName));
        }
        for (final String attrName : StandardDOMEventAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardDOMEventAttributeTagProcessor(dialectPrefix, attrName));
        }
        processors.add(new StandardEachTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardFragmentTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardHrefTagProcessor(dialectPrefix));
        processors.add(new StandardIfTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardIncludeTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardInlineHTMLTagProcessor(dialectPrefix));
        processors.add(new StandardInsertTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardLangXmlLangTagProcessor(dialectPrefix));
        processors.add(new StandardMethodTagProcessor(dialectPrefix));
        for (final String attrName : StandardNonRemovableAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardNonRemovableAttributeTagProcessor(dialectPrefix, attrName));
        }
        processors.add(new StandardObjectTagProcessor(TemplateMode.HTML, dialectPrefix));
        for (final String attrName : StandardRemovableAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardRemovableAttributeTagProcessor(dialectPrefix, attrName));
        }
        processors.add(new StandardRemoveTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardReplaceTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardSrcTagProcessor(dialectPrefix));
        processors.add(new StandardStyleappendTagProcessor(dialectPrefix));
        processors.add(new StandardSubstituteByTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardSwitchTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardUnlessTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardUtextTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardValueTagProcessor(dialectPrefix));
        processors.add(new StandardWithTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardXmlBaseTagProcessor(dialectPrefix));
        processors.add(new StandardXmlLangTagProcessor(dialectPrefix));
        processors.add(new StandardXmlSpaceTagProcessor(dialectPrefix));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardRefAttributeTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new StandardDefaultAttributesTagProcessor(TemplateMode.HTML, dialectPrefix));

        /*
         * HTML: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(TemplateMode.HTML, dialectPrefix, StandardBlockTagProcessor.ELEMENT_NAME));

        /*
         * HTML: TEXT PROCESSORS
         *
         * NOTE the ability of the Standard Inlining mechanism to directly write to output instead of generating
         * internal Strings relies on the fact that there is only ONE ITextProcessor instance for each
         * template mode in the StandardDialect (see AbstractStandardInliner for details). So if new processors
         * are added here, it should be for a really compelling reason.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningTextProcessor(TemplateMode.HTML));

        /*
         * HTML: CDATASection PROCESSORS
         *
         * NOTE as happens with text processors, adding a processor here would convert models in non-reshapable.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningCDATASectionProcessor(TemplateMode.HTML));

        /*
         * HTML: DOCTYPE PROCESSORS
         */
        processors.add(new StandardTranslationDocTypeProcessor());

        /*
         * HTML: COMMENT PROCESSORS
         *
         * NOTE as happens with text processors, adding a processor here would convert models in non-reshapable.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningCommentProcessor(TemplateMode.HTML));
        processors.add(new StandardConditionalCommentProcessor());

        /*
         * HTML: TEMPLATE BOUNDARIES PROCESSORS
         */
        processors.add(new StandardInlineEnablementTemplateBoundariesProcessor(TemplateMode.HTML));



        /*
         * ------------------------
         * ------------------------
         * XML TEMPLATE MODE
         * ------------------------
         * ------------------------
         */


        /*
         * XML: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardAssertTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardAttrTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardAttrappendTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardAttrprependTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardCaseTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardEachTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardFragmentTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardIfTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardIncludeTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardInlineXMLTagProcessor(dialectPrefix));
        processors.add(new StandardInsertTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardObjectTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardRemoveTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardReplaceTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardSubstituteByTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardSwitchTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardUnlessTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardUtextTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardWithTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardRefAttributeTagProcessor(TemplateMode.XML, dialectPrefix));
        processors.add(new StandardDefaultAttributesTagProcessor(TemplateMode.XML, dialectPrefix));

        /*
         * XML: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(TemplateMode.XML, dialectPrefix, StandardBlockTagProcessor.ELEMENT_NAME));

        /*
         * XML: TEXT PROCESSORS
         *
         * NOTE the ability of the Standard Inlining mechanism to directly write to output instead of generating
         * internal Strings relies on the fact that there is only ONE ITextProcessor instance for each template mode
         * in the StandardDialect (see AbstractStandardInliner for details). So if new processors are added here,
         * it should be for a really compelling reason.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningTextProcessor(TemplateMode.XML));

        /*
         * XML: CDATASection PROCESSORS
         *
         * NOTE as happens with text processors, adding a processor here would convert models in non-reshapable.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningCDATASectionProcessor(TemplateMode.XML));

        /*
         * XML: COMMENT PROCESSORS
         *
         * NOTE as happens with text processors, adding a processor here would convert models in non-reshapable.
         * See EngineConfiguration#isModelReshapable()
         */
        processors.add(new StandardInliningCommentProcessor(TemplateMode.XML));

        /*
         * XML: TEMPLATE BOUNDARIES PROCESSORS
         */
        processors.add(new StandardInlineEnablementTemplateBoundariesProcessor(TemplateMode.XML));



        /*
         * ------------------------
         * ------------------------
         * TEXT TEMPLATE MODE
         * ------------------------
         * ------------------------
         */


        /*
         * TEXT: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardAssertTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardCaseTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardEachTagProcessor(TemplateMode.TEXT, dialectPrefix));
        // No th:fragment attribute in text modes: no fragment selection available!
        processors.add(new StandardIfTagProcessor(TemplateMode.TEXT, dialectPrefix));
        // No th:include to be added here, as it is already deprecated since 3.0
        processors.add(new StandardInlineTextualTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardInsertTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardObjectTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardRemoveTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardReplaceTagProcessor(TemplateMode.TEXT, dialectPrefix));
        // No th:substituteby to be added here, as it is already deprecated since 2.1
        processors.add(new StandardSwitchTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardUnlessTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardUtextTagProcessor(TemplateMode.TEXT, dialectPrefix));
        processors.add(new StandardWithTagProcessor(TemplateMode.TEXT, dialectPrefix));

        /*
         * TEXT: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(TemplateMode.TEXT, dialectPrefix, StandardBlockTagProcessor.ELEMENT_NAME));
        processors.add(new StandardBlockTagProcessor(TemplateMode.TEXT, null, "")); // With no name, will process [# th....] elements

        /*
         * TEXT: TEXT PROCESSORS
         *
         * NOTE the ability of the Standard Inlining mechanism to directly write to output instead of generating
         * internal Strings relies on the fact that there is only ONE ITextProcessor instance for each template mode
         * in the StandardDialect (see AbstractStandardInliner for details). So if new processors are added here,
         * it should be for a really compelling reason.
         */
        processors.add(new StandardInliningTextProcessor(TemplateMode.TEXT));

        /*
         * TEXT: TEMPLATE BOUNDARIES PROCESSORS
         */
        processors.add(new StandardInlineEnablementTemplateBoundariesProcessor(TemplateMode.TEXT));



        /*
         * ------------------------
         * ------------------------
         * JAVASCRIPT TEMPLATE MODE
         * ------------------------
         * ------------------------
         */


        /*
         * JAVASCRIPT: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardAssertTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardCaseTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardEachTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        // No th:fragment attribute in text modes: no fragment selection available!
        processors.add(new StandardIfTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        // No th:include to be added here, as it is already deprecated since 3.0
        processors.add(new StandardInlineTextualTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardInsertTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardObjectTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardRemoveTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardReplaceTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        // No th:substituteby to be added here, as it is already deprecated since 2.1
        processors.add(new StandardSwitchTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardUnlessTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardUtextTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));
        processors.add(new StandardWithTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix));

        /*
         * JAVASCRIPT: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(TemplateMode.JAVASCRIPT, dialectPrefix, StandardBlockTagProcessor.ELEMENT_NAME));
        processors.add(new StandardBlockTagProcessor(TemplateMode.JAVASCRIPT, null, "")); // With no name, will process [# th....] elements

        /*
         * JAVASCRIPT: TEXT PROCESSORS
         *
         * NOTE the ability of the Standard Inlining mechanism to directly write to output instead of generating
         * internal Strings relies on the fact that there is only ONE ITextProcessor instance for each template mode
         * in the StandardDialect (see AbstractStandardInliner for details). So if new processors are added here,
         * it should be for a really compelling reason.
         */
        processors.add(new StandardInliningTextProcessor(TemplateMode.JAVASCRIPT));

        /*
         * JAVASCRIPT: TEMPLATE BOUNDARIES PROCESSORS
         */
        processors.add(new StandardInlineEnablementTemplateBoundariesProcessor(TemplateMode.JAVASCRIPT));



        /*
         * ------------------------
         * ------------------------
         * CSS TEMPLATE MODE
         * ------------------------
         * ------------------------
         */


        /*
         * CSS: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardAssertTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardCaseTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardEachTagProcessor(TemplateMode.CSS, dialectPrefix));
        // No th:fragment attribute in text modes: no fragment selection available!
        processors.add(new StandardIfTagProcessor(TemplateMode.CSS, dialectPrefix));
        // No th:include to be added here, as it is already deprecated since 3.0
        processors.add(new StandardInlineTextualTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardInsertTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardObjectTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardRemoveTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardReplaceTagProcessor(TemplateMode.CSS, dialectPrefix));
        // No th:substituteby to be added here, as it is already deprecated since 2.1
        processors.add(new StandardSwitchTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardTextTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardUnlessTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardUtextTagProcessor(TemplateMode.CSS, dialectPrefix));
        processors.add(new StandardWithTagProcessor(TemplateMode.CSS, dialectPrefix));

        /*
         * CSS: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(TemplateMode.CSS, dialectPrefix, StandardBlockTagProcessor.ELEMENT_NAME));
        processors.add(new StandardBlockTagProcessor(TemplateMode.CSS, null, "")); // With no name, will process [# th....] elements

        /*
         * CSS: TEXT PROCESSORS
         *
         * NOTE the ability of the Standard Inlining mechanism to directly write to output instead of generating
         * internal Strings relies on the fact that there is only ONE ITextProcessor instance for each template mode
         * in the StandardDialect (see AbstractStandardInliner for details). So if new processors are added here,
         * it should be for a really compelling reason.
         */
        processors.add(new StandardInliningTextProcessor(TemplateMode.CSS));

        /*
         * CSS: TEMPLATE BOUNDARIES PROCESSORS
         */
        processors.add(new StandardInlineEnablementTemplateBoundariesProcessor(TemplateMode.CSS));


        /*
         * ------------------------
         * ------------------------
         * RAW TEMPLATE MODE
         * ------------------------
         * ------------------------
         */

        // No processors defined for template mode. Note only TextProcessors would be possible in this template mode,
        // given the entire templates are considered Text.


        return processors;
    }
}
