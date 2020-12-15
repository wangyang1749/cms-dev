package com.wangyang.pojo.params;

import com.wangyang.pojo.enums.ArticleStatus;
import lombok.Data;

@Data
public class ArticleQuery {
    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Post status.
     */
    private ArticleStatus status;

    /**
     * Category id.
     */
    private Integer categoryId;

    public Integer userId;

    public ArticleStatus articleStatus;

    public Integer tagsId;

    private Boolean haveHtml;

}
