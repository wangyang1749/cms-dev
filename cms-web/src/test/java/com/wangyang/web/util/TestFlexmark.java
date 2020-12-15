package com.wangyang.web.util;

import com.google.common.base.Joiner;



import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class TestFlexmark {
//    static final DataHolder OPTIONS_PDF = PegdownOptionsAdapter.flexmarkOptions(
//            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
//            , TocExtension.create()).toMutable()
//            .set(TocExtension.LIST_CLASS, PdfConverterExtension.DEFAULT_TOC_LIST_CLASS)
//            .toImmutable();

    private static final DataHolder OPTIONS = new MutableDataSet();
//            .set(Parser.EXTENSIONS, Arrays.asList(
//                    AttributesExtension.create(),
//                    AutolinkExtension.create(),
//                    EmojiExtension.create(),
////                    EscapedCharacterExtension.create(),
////                    StrikethroughExtension.create(),
////                    TaskListExtension.create(),
////                    InsExtension.create(),
////                    MediaTagsExtension.create(),
//                    TablesExtension.create(),
//                    GitLabExtension.create(),
//                    TocExtension.create()
////                    YamlFrontMatterExtension.create(),

//            ));
//            .set(TocExtension.LEVELS, 255)
//            .set(GitLabExtension.INLINE_MATH_PARSER, true)
//            .set(GitLabExtension.RENDER_BLOCK_MATH, true);

//
//            .set(TablesExtension.WITH_CAPTION, false)
//            .set(TablesExtension.COLUMN_SPANS, false)
//            .set(TablesExtension.MIN_SEPARATOR_DASHES, 1)
//            .set(TablesExtension.MIN_HEADER_ROWS, 1)
//            .set(TablesExtension.MAX_HEADER_ROWS, 1)
//            .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
//            .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
//            .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
    //            .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.EMOJI_CHEAT_SHEET)
    //            .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.UNICODE_ONLY)
//            .set(HtmlRenderer.SOFT_BREAK, "<br />\n");

//    static final DataHolder OPTIONS = new MutableDataSet()
//            .set(Parser.REFERENCES_KEEP, KeepType.LAST)
//            .set(HtmlRenderer.INDENT_SIZE, 2)
//            .set(HtmlRenderer.PERCENT_ENCODE_URLS, true)
//
//            // for full GFM table compatibility add the following table extension options:
//            .set(TablesExtension.COLUMN_SPANS, false)
//            .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
//            .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
//            .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
//            .set(GitLabExtension.INLINE_MATH_PARSER, true)
//            .set(GitLabExtension.RENDER_BLOCK_MATH, true)
//            .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()))
//
//            .toImmutable();
    @Test
    public void text1(){
//        MutableDataSet options = new MutableDataSet();
        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
//
//        Parser parser = Parser.builder(OPTIONS).build();
//        HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();
//        // You can re-use parser and renderer instances
//        Node document = parser.parse("$$\n" +
//                "x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}.\n" +
//                "$$");
//
//        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
//        System.out.println(html);
    }

    @Test
    public void test2() throws Exception{
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test.md");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

        List<String> list = reader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(list);
        HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();

        Parser parser = Parser.builder(OPTIONS).build();
        // You can re-use parser and renderer instances
//        Node document = parser.parse(content);
//        String html = renderer.render(document);
////        System.out.println(html);

        Node document2 = parser.parse(content);
        String html2 = renderer.render(document2);
        System.out.println(html2);
    }

    @Test
    public void test(){
//        String html = PdfConverterExtension.embedCss(getContent("index.html"), getContent("index.css"));
////        Parser PARSER = Parser.builder(OPTIONS).build();
////        HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();
////        Node document = PARSER.parse(pegdown);
////        String html = RENDERER.render(document);
////
////        builder.useSVGDrawer();
//        new BatikSVGDrawer();
//        String nonLatinFonts = "" ;
//        PdfConverterExtension.exportToPdf("/home/wy/Documents/cms/test/flexmark-java.pdf", html, "", OPTIONS_PDF);
////        OPTIONS.toMutable().set(PdfConverterExtension.PROTECTION_POLICY, new StandardProtectionPolicy("opassword", "upassword", new AccessPermission()));
//        PdfConverterExtension.exportToPdf("/home/wy/Documents/cms/test/flexmark-java.pdf", html, "", OPTIONS);
    }

    @Test
    public void test5() throws  Exception{
//        try (OutputStream os = new FileOutputStream("/home/wy/Documents/cms/test/flexmark-java.pdf")) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.useFastMode();
//            builder.useSVGDrawer(new BatikSVGDrawer());
//            builder.addDOMMutator(LaTeXDOMMutator.INSTANCE); // Converts Latex to XHTML and MathML
//            builder.useMathMLDrawer(new MathMLDrawer());  // Renders MathML, not needed if you don't use math.
//            builder.useFont(new FSSupplier<InputStream>() {
//                @Override
//                public InputStream supply() {
//                    try {
//                        return new FileInputStream("/home/wy/Documents/cms/test/YaHei.Consolas.1.12.ttf");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            }, "ya-hei", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
////            builder.withW3cDocument(html5ParseDocument("",2),"");
//            builder.withUri("file:///home/wy/Documents/cms/cms-web/src/test/resources/index.html");
//            builder.toStream(os);
//            builder.run();
//        }
    }

    @Test
    public void test4(){
//        String html = PdfConverterExtension.embedCss(getContent("index.html"), getContent("index.css"));
//        System.out.println(html);
    }

    public String getContent(String fileName){
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

            List<String> list = reader.lines().collect(Collectors.toList());
            String content = Joiner.on("\n").join(list);
            return content;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public org.w3c.dom.Document html5ParseDocument(String urlStr, int timeoutMs) throws IOException
    {
//        URL url = new URL(urlStr);
//        org.jsoup.nodes.Document doc;
//
//        if (url.getProtocol().equalsIgnoreCase("file")) {
//            doc = Jsoup.parse(new File(url.getPath()), "UTF-8");
//        }
//        else {
//            doc = Jsoup.parse(url, timeoutMs);
//        }
//        // Should reuse W3CDom instance if converting multiple documents.
//        return new W3CDom().fromJsoup(doc);
        return null;
    }
}
