package com.wangyang.common.flexmark.imgattr;



import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.html.Attribute;
import com.vladsch.flexmark.util.html.MutableAttributes;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.jetbrains.annotations.NotNull;


public class AttributeExtension implements HtmlRenderer.HtmlRendererExtension {
    @Override
    public void rendererOptions(@NotNull MutableDataHolder options) {
        // add any configuration settings to options you want to apply to everything, here
    }

    @Override
    public void extend(HtmlRenderer.@NotNull Builder builder, @NotNull String s) {
        builder.attributeProviderFactory(SampleAttributeProvider.Factory());
//        builder.nodeRendererFactory()
    }

    public  static AttributeExtension create() {
        return new AttributeExtension();
    }
}
 class SampleAttributeProvider implements AttributeProvider {
     @Override
     public void setAttributes(@NotNull Node node, @NotNull AttributablePart attributes, @NotNull MutableAttributes part) {
         if (node instanceof Image) {
             Image link = (Image) node;

//             link.setText(BasedSequence.NULL);
         }
//         if (node instanceof Image && part == AttributablePart.LINK) {
//            attributes.replaceValue("class", "lazy");
//            attributes.replaceValue("data-original",((Image) node).getUrl());
//            attributes.remove("src");
//        }
     }

     //    @Override
//    public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull Attributes attributes) {
//        if (node instanceof Image && part == AttributablePart.LINK) {
//            attributes.replaceValue("class", "lazy");
//            attributes.replaceValue("data-original",((Image) node).getUrl());
//            attributes.remove("src");
//        }
//    }
//
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