package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Category;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页的文章列表
 */
@Data
public class CategoryArticleListDao {

    private String viewName;
    private Page<ArticleDto> page;
    private Category category;
    private List<Category> children;
    private Category parent;
    /**
     * 第一个category文章列表的路径,
     * 相应的录路径格式在 CategoryServiceImpl#listCategoryVo()生成
     * 每一个CategoryVo包含linkPath：
     * @see com.wangyang.pojo.vo.CategoryVO#setLinkPath(String)
     * TemplateUtil.convertHtmlAndSave(categoryArticle, template.get());
     */
    private String path;
    /**
     * url路径的格式
     * <li class="page-item" th:each="item : ${#numbers.sequence(2, view.page.totalPages)}">
     *   <a class="page-link"  th:href="${view.linkPath}+'/'+${item}+'/page.html'" th:text="${item}">1</a>
     * </li>
     */
    private String linkPath;
}
