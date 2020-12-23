package com.wangyang.common.flexmark.gitlab.internal;

import com.wangyang.common.flexmark.gitlab.GitLabInlineMath;
import com.vladsch.flexmark.parser.InlineParser;
import com.vladsch.flexmark.parser.InlineParserExtension;
import com.vladsch.flexmark.parser.InlineParserExtensionFactory;
import com.vladsch.flexmark.parser.LightInlineParser;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitLabInlineMathParser implements InlineParserExtension {
    Pattern MATH_PATTERN = Pattern.compile("\\$`((?:.|\n)*?)`\\$");
    Pattern MATH_PATTERN_DOUBLE = Pattern.compile("\\$\\$((?:.|\n)*?)\\$\\$");
    Pattern MATH_PATTERN_ONE = Pattern.compile("\\$((?:.|\n)*?)\\$");

    public GitLabInlineMathParser(LightInlineParser inlineParser) {
    }

    @Override
    public void finalizeDocument(@NotNull InlineParser inlineParser) {

    }

    @Override
    public void finalizeBlock(@NotNull InlineParser inlineParser) {

    }

    @Override
    public boolean parse(@NotNull LightInlineParser inlineParser) {
//        if (inlineParser.peek(1) == '`') {

        if (inlineParser.peek() =='$') {
            BasedSequence input = inlineParser.getInput();
            Matcher matcher = inlineParser.matcher(MATH_PATTERN);
            Matcher matcherDouble = inlineParser.matcher(MATH_PATTERN_DOUBLE);
            Matcher matcherOne = inlineParser.matcher(MATH_PATTERN_ONE);
            if (matcher != null) {
                inlineParser.flushTextNode();

                BasedSequence mathOpen = input.subSequence(matcher.start(), matcher.start(1));
                BasedSequence mathClosed = input.subSequence(matcher.end(1), matcher.end());
                GitLabInlineMath inlineMath = new GitLabInlineMath(mathOpen, mathOpen.baseSubSequence(mathOpen.getEndOffset(), mathClosed.getStartOffset()), mathClosed);
                inlineParser.getBlock().appendChild(inlineMath);
                return true;
            }else if(matcherDouble!=null){
                inlineParser.flushTextNode();

                BasedSequence mathOpen = input.subSequence(matcherDouble.start(), matcherDouble.start(1));
                BasedSequence mathClosed = input.subSequence(matcherDouble.end(1), matcherDouble.end());
                GitLabInlineMath inlineMath = new GitLabInlineMath(mathOpen, mathOpen.baseSubSequence(mathOpen.getEndOffset(), mathClosed.getStartOffset()), mathClosed);
                inlineParser.getBlock().appendChild(inlineMath);
                return true;
            }else if(matcherOne!=null){
                inlineParser.flushTextNode();

                BasedSequence mathOpen = input.subSequence(matcherOne.start(), matcherOne.start(1));
                BasedSequence mathClosed = input.subSequence(matcherOne.end(1), matcherOne.end());
                GitLabInlineMath inlineMath = new GitLabInlineMath(mathOpen, mathOpen.baseSubSequence(mathOpen.getEndOffset(), mathClosed.getStartOffset()), mathClosed);
                inlineParser.getBlock().appendChild(inlineMath);
                return true;
            }
        }
        return false;
    }

    public static class Factory implements InlineParserExtensionFactory {
        @Nullable
        @Override
        public Set<Class<?>> getAfterDependents() {
            return null;
        }

        @NotNull
        @Override
        public CharSequence getCharacters() {
            return "$";
        }

        @Nullable
        @Override
        public Set<Class<?>> getBeforeDependents() {
            return null;
        }

        @NotNull
        @Override
        public InlineParserExtension apply(@NotNull LightInlineParser lightInlineParser) {
            return new GitLabInlineMathParser(lightInlineParser);
        }

        @Override
        public boolean affectsGlobalScope() {
            return false;
        }
    }
}
