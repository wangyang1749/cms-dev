package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.vo.ArticleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer>
        , JpaSpecificationExecutor<Article> {

    /**
     * update article likes by article id
     * @param id
     * @return
     */
    @Query("update Article a set a.likes = a.likes+1 where a.id = :aid")
    @Modifying
    int updateLikes(@Param("aid") int id);

    @Query("select o from Article o where o.id in (select a.articleId from ArticleCategory a where a.categoryId=?1)")
    List<Article> findByCategoryId(int id);
}
