package com.wangyang.cms.utils;


//import com.vladsch.flexmark.ext.tables.TablesExtension;
//import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.media.tags.MediaTagsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
//import com.wangyang.cms.gitlab.GitLabExtension;
//import com.wangyang.cms.media.tags.MediaTagsExtension;
import com.wangyang.cms.pojo.support.CmsConst;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;


public class MarkdownUtils {
    private static final DataHolder OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS,Arrays.asList(
                    EmojiExtension.create(),
                    TablesExtension.create(),
                    GitLabExtension.create(),
                    TocExtension.create(),
                    MediaTagsExtension.create()

            )).set(TocExtension.LEVELS, 255)
            .set(GitLabExtension.INLINE_MATH_PARSER, true)
            .set(GitLabExtension.RENDER_BLOCK_MATH, true);

    private static final Parser PARSER = Parser.builder(OPTIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    public static String renderHtml(String markdown) {
        if(StringUtils.isBlank(markdown)){
            return StringUtils.EMPTY;
        }
        Node document = PARSER.parse(markdown);

        String render = RENDERER.render(document);

        if (render.contains(CmsConst.MARKDOWN_REVEAL_START)) {
            render = render.replaceAll(CmsConst.MARKDOWN_REVEAL_START, CmsConst.LABEL_SECTION_START);
        }
        if (render.contains(CmsConst.MARKDOWN_REVEAL_END)) {
            render = render.replaceAll(CmsConst.MARKDOWN_REVEAL_END, CmsConst.LABEL_SECTION_END);
        }

        return render;
    }
}
