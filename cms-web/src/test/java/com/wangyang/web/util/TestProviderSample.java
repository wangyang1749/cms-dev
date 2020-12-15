package com.wangyang.web.util;


import com.google.common.base.Joiner;
import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.html.*;
import com.vladsch.flexmark.html.renderer.*;
import com.vladsch.flexmark.parser.*;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.*;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.sequence.BasedSequence;
//import com.wangyang.cms.gitlab.GitLabInlineMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class TestInline  implements InlineParserExtension{
    Pattern MATH_PATTERN = Pattern.compile("\\$`((?:.|\n)*?)`\\$");
    TestInline( LightInlineParser lightInlineParser){

    }
    @Override
    public void finalizeDocument(@NotNull InlineParser inlineParser) {

    }

    @Override
    public void finalizeBlock(@NotNull InlineParser inlineParser) {

    }

    @Override
    public boolean parse(@NotNull LightInlineParser inlineParser) {
        if (inlineParser.peek(1) == '`') {
            BasedSequence input = inlineParser.getInput();
            Matcher matcher = inlineParser.matcher(MATH_PATTERN);
            if (matcher != null) {
                inlineParser.flushTextNode();

//                BasedSequence mathOpen = input.subSequence(matcher.start(), matcher.start(1));
//                BasedSequence mathClosed = input.subSequence(matcher.end(1), matcher.end());
//
//                GitLabInlineMath inlineMath = new GitLabInlineMath(mathOpen, mathOpen.baseSubSequence(mathOpen.getEndOffset(), mathClosed.getStartOffset()), mathClosed);
//                System.out.println(mathOpen.baseSubSequence(mathOpen.getEndOffset(), mathClosed.getStartOffset()));
//                inlineParser.getBlock().appendChild(inlineMath);
//                System.out.println(inlineMath.getText());
                return true;
            }
        }
        return false;
    }

    static class Factory implements InlineParserExtensionFactory {
        @Override
        public @NotNull CharSequence getCharacters() {
            return "$";
        }

        @Override
        public @NotNull InlineParserExtension apply(@NotNull LightInlineParser lightInlineParser) {
            return new TestInline(lightInlineParser);
        }

        @Override
        public @Nullable Set<Class<?>> getAfterDependents() {
            return null;
        }

        @Override
        public @Nullable Set<Class<?>> getBeforeDependents() {
            return null;
        }

        @Override
        public boolean affectsGlobalScope() {
            return false;
        }
    }

}


//class TestOptions implements MutableDataSetter {
//    final public String inlineMathClass;
//    final public boolean inlineMathParser;
//    TestOptions(DataHolder options){
//        inlineMathClass = "katex7777";
//        inlineMathParser = true;
//    }
//    @Override
//    public @NotNull MutableDataHolder setIn(@NotNull MutableDataHolder mutableDataHolder) {
//        mutableDataHolder.set(GitLabExtension.INLINE_MATH_PARSER, inlineMathParser);
//        mutableDataHolder.set(GitLabExtension.INLINE_MATH_CLASS, inlineMathClass);
//        return mutableDataHolder;
//    }
//}

/**
 * 渲染函数
 */
class TestNodeRenderer implements NodeRenderer{
//    final TestOptions options;
//
//    TestNodeRenderer(DataHolder options) {
//        this.options = new TestOptions(options);
//    }
//-------------------------------------

    @Override
    public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
//        Set<NodeRenderingHandler<?>> set = new HashSet<>();
//        set.add(new NodeRenderingHandler<>(GitLabInlineMath.class, TestNodeRenderer.this::render));
        return null;
    }

//    private void render(GitLabInlineMath node, NodeRendererContext context, HtmlWriter html) {
//        html.withAttr().attr(Attribute.CLASS_ATTR, "options.inlineMathClass").withAttr().tag("span");
//        String math = node.getText().toString();
//        System.out.println(math);
////        html.text(generateMath(node.getText().toString()));
////        html.text(generateMathByCms());
//        html.text(SocketUtil.sendNodeJs(node.getText().toString()));
////        html.text(node.getText());
//        System.out.println("#########################3");
//        html.tag("/span");
//    }

    public String generateMathByCms(){
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("node  /home/wy/Documents/cms/src/test/resources/03.js");//这里我的codes.js是保存在c盘下面的phantomjs目录
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sbf = new StringBuffer();
            String tmp = "";
            while((tmp = br.readLine())!=null){
                sbf.append(tmp);
            }
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateMath(String  math){
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("test2.js");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            List<String> list = reader.lines().collect(Collectors.toList());
            String content = Joiner.on("\n").join(list);

            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval("print('hello');"); // execute js from source
            engine.eval(content); // execute js from file
            Object eval = engine.eval("function getHtml(){var html = katex.renderToString('c = \\\\pm\\\\sqrt{a^2 + b^2}');return html}");
            Invocable in = (Invocable) engine;
            Object getHtml = ((Invocable) engine).invokeFunction("getHtml");
            return (String)getHtml;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
//-------------------------------------------------

    public static class Factory implements NodeRendererFactory {
        @NotNull
        @Override
        public NodeRenderer apply(@NotNull DataHolder options) {
//            return new TestNodeRenderer(options);
            return new TestNodeRenderer();
        }
    }
}




public class TestProviderSample {
    static class SampleExtension implements HtmlRenderer.HtmlRendererExtension, Parser.ParserExtension

    {

//        final public static DataKey<String> INLINE_MATH_CLASS = new DataKey<>("INLINE_MATH_CLASS", "katex");

        @Override
        public void rendererOptions(@NotNull final MutableDataHolder options) {
            // add any configuration settings to options you want to apply to everything, here
        }

        @Override
        public void extend(final HtmlRenderer.Builder rendererBuilder, @NotNull final String rendererType) {
//            rendererBuilder.attributeProviderFactory(SampleAttributeProvider.Factory());
            rendererBuilder.nodeRendererFactory(new TestNodeRenderer.Factory());
        }

        static SampleExtension create() {
            return new SampleExtension();
        }

        @Override
        public void parserOptions(MutableDataHolder mutableDataHolder) {

        }

        @Override
        public void extend(Parser.Builder builder) {
            builder.customInlineParserExtensionFactory(new TestInline.Factory());
        }
    }



    static class SampleAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(@NotNull final Node node, @NotNull final AttributablePart part, @NotNull final Attributes attributes) {
            if (node instanceof AutoLink && part == AttributablePart.LINK) {
                // Put info in custom attribute instead
                attributes.replaceValue("class", "my-autolink-class1");
            }
        }

        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @Override
                public @NotNull AttributeProvider apply(@NotNull LinkResolverContext linkResolverContext) {
                    return new SampleAttributeProvider();
                }


                public AttributeProvider create(NodeRendererContext context) {
                    //noinspection ReturnOfInnerClass
                    return new SampleAttributeProvider();
                }
            };
        }
    }

    static String commonMark(String markdown) {
        MutableDataHolder options = new MutableDataSet();
        options.set(Parser.EXTENSIONS,Arrays.asList( SampleExtension.create()));

        // change soft break to hard break
        options.set(HtmlRenderer.SOFT_BREAK, "<br/>");

        Parser parser = Parser.builder(options).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        final String html = renderer.render(document);
        return html;
    }

    public static void main(String[] args) {
        String html = commonMark("http://github.com/vsch/flexmark-java");
        System.out.println(html); // output: <p><a href="http://github.com/vsch/flexmark-java" class="my-autolink-class">http://github.com/vsch/flexmark-java</a></p>

        html = commonMark(" $`a^2+b^2=c^2`$");
        System.out.println(html); // output: <p>hello<br/>world</p>
    }
}