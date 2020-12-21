package com.wangyang.common.flexmark.gitlab;

import com.vladsch.flexmark.util.sequence.BasedSequence;

/**
 * A Ins node
 */
public class GitLabIns extends GitLabInline {
    public GitLabIns() {
    }

    public GitLabIns(BasedSequence chars) {
        super(chars);
    }

    public GitLabIns(BasedSequence openingMarker, BasedSequence text, BasedSequence closingMarker) {
        super(openingMarker, text, closingMarker);
    }
}
