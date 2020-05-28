package com.wangyang.common.flexmark.media;

import com.vladsch.flexmark.util.ast.VisitHandler;

public class PictureLinkVisitorExt {
    public static <V extends PictureLinkVisitor> VisitHandler<?>[] VISIT_HANDLERS(V visitor) {
        return new VisitHandler<?>[] {
                new VisitHandler<>(PictureLink.class, visitor::visit)
        };
    }
}
