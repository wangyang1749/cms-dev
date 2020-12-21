package com.wangyang.pojo.params;

import com.wangyang.pojo.dto.InputConverter;
import com.wangyang.pojo.entity.Article;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ArticleParams  implements InputConverter<Article> {

    private String templateName;
//    private ArticleStatus status =ArticleStatus.PUBLISHED;
//    private Boolean haveHtml;

    @NotBlank(message = "文章标题不能为空!!")
    private String title;
    @NotBlank(message = "文章内容不能为空!!")
    private String originalContent;
    private String summary;
//    private String viewName;
    private Set<Integer> tagIds;
    @NotNull(message = "文章类别不能为空!!")
    private Integer categoryId;
//    @NotNull(message = "文章用户不能为空!!")
//    private Integer userId;
//    private String  path;
    private String picPath;


}
