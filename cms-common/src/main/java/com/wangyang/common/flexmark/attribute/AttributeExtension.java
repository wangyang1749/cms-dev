package com.wangyang.common.flexmark.attribute;

import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.html.Attributes;

import javax.validation.constraints.NotNull;


public class AttributeExtension implements HtmlRenderer.HtmlRendererExtension {
    @Override
    public void rendererOptions(@NotNull MutableDataHolder options) {
        // add any configuration settings to options you want to apply to everything, here
    }

    @Override
    public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
        htmlRendererBuilder.attributeProviderFactory(SampleAttributeProvider.Factory());
    }

    public  static AttributeExtension create() {
        return new AttributeExtension();
    }
}
 class SampleAttributeProvider implements AttributeProvider {
    @Override
    public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull Attributes attributes) {
        if (node instanceof Image && part == AttributablePart.LINK) {
            attributes.replaceValue("class", "lazy");
            attributes.replaceValue("data-original",((Image) node).getUrl());
            attributes.remove("src");
        }
    }

    static AttributeProviderFactory Factory() {
        return new IndependentAttributeProviderFactory() {
            @NotNull
            @Override
            public AttributeProvider apply(@NotNull LinkResolverContext context) {
                return new SampleAttributeProvider();
            }
        };
    }
}