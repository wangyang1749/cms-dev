package com.wangyang.cms.core.jms;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.ArticleCategory;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.*;

@Component
@Slf4j
public class CmsService {
    @Autowired
    ITemplateService templateService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;


    /**
     * 根据Category,生成这个分类第一页的Html
     * @param category
     */
    public void generateCategoryArticleListByCategory(Category category){
        if(category.getHaveHtml()){
            //生成子 节点的静态页面
            convertHtml(category);
            //判断是否存在父节点
            if(category.getParentId()!=0){
                Optional<Category> optionalCategory = categoryRepository.findById(category.getParentId());
                if(optionalCategory.isPresent()){
                    Category  parentCategory =optionalCategory.get();
                    parentCategory.setHaveChildren(true);
                    Category saveCategoryParent = categoryRepository.save(parentCategory);
                    //重新生成父节点的静态页面
                    convertHtml(saveCategoryParent);
                }

            }
        }
    }


    public void updateCategoryPage(List<Category> categories,Collection<Integer> categoryIds){
        Set<Integer> oldIds = ServiceUtil.fetchProperty(categories, Category::getId);
        Collection<Integer> oldIdsNotExists =new ArrayList(oldIds);
        Map<Integer, Category> categoryMap = ServiceUtil.convertToMap(categories, Category::getId);

        //得到新分类没有的集合

        if(!CollectionUtils.isEmpty(oldIdsNotExists)&&!CollectionUtils.isEmpty(categoryIds)){
            oldIdsNotExists.removeAll(categoryIds);
            oldIdsNotExists.forEach(id->{
                System.out.println(categoryMap.get(id));
                generateCategoryArticleListByCategory(categoryMap.get(id));

                log.debug("跟新id为:"+id+", 的Category!!");
            });
        }
    }

    /**
     *
     * @param id
     */
    public void generateCategoryArticleListByCategory(Integer id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()){
            generateCategoryArticleListByCategory(categoryOptional.get());
        }

    }

    /**
     * generate article list title info`
     * @param category
     */
    private void convertHtml(Category category) {
        /**
         * TODO 没有使用jms时转换
         */
        CategoryArticleListDao articleListVo = getArticleListByCategory(category);
        log.debug("生成"+category.getName()+"分类下的第一个页面!");
        Template template = templateService.findById(category.getTemplateId());
        TemplateUtil.convertHtmlAndSave(articleListVo,template);
    }

    /**
     * 获取第一页文章和分类, 用于生成HTML
     *  PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
     * @param category
     * @return
     */
    public CategoryArticleListDao getArticleListByCategory(Category category){
        CategoryArticleListDao categoryArticleListDao = getArticleListByCategory(category, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
        if(category.getHaveChildren()){
            List<Category> categoryList = categoryRepository.findAll(new Specification<Category>() {
                @Override
                public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return criteriaQuery.where(criteriaBuilder.equal(root.get("parentId"), category.getId())
                            ,criteriaBuilder.isTrue(root.get("haveHtml"))).getRestriction();

                }
            });
            categoryArticleListDao.setChildren(categoryList);
            log.debug("##这个category有子节点!!");
        }
        if(category.getParentId()!=0){
            List<Category> categoryList = categoryRepository.findAll(new Specification<Category>() {
                @Override
                public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    return criteriaBuilder.equal(root.get("id"), category.getParentId());
                }
            });

            categoryArticleListDao.setParent(categoryList.get(0));
            log.debug("##这个category有父节点!!");
        }

        return categoryArticleListDao;
    }

    /**
     * 动态分页使用
     * @param category
     * @param pageable
     * @return
     */
    public CategoryArticleListDao getArticleListByCategory(Category category, Pageable pageable){
        CategoryArticleListDao articleListVo = new CategoryArticleListDao();
        Page<ArticleDto> articleDtoPage = findArticleListByCategoryId(category.getId(), pageable);
        articleListVo.setPage(articleDtoPage);
        articleListVo.setCategory(category);
        articleListVo.setViewName(category.getViewName());
        return articleListVo;
    }


    /**
     * 查找分类第一页的文章,用于该分类下文章的静态化
     * @param categoryId
     * @param pageable
     * @return
     */
    public Page<ArticleDto> findArticleListByCategoryId(int categoryId,Pageable pageable) {
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Subquery<ArticleCategory> subquery = criteriaQuery.subquery(ArticleCategory.class);
                Root<ArticleCategory> subRoot = subquery.from(ArticleCategory.class);
                subquery  = subquery.select(subRoot.get("articleId"))
                        .where(criteriaBuilder.equal(subRoot.get("categoryId"),categoryId));
                return root.get("id").in(subquery);
            }
        };
        Page<Article> articles = articleRepository.findAll(specification, pageable);
        return  articles.map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article,articleDto);
            return articleDto;
        });
    }

}
