package com.wangyang.data.service;

import com.wangyang.model.pojo.dto.TagsDto;
import com.wangyang.model.pojo.entity.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITagsService {

    /**
     * list tags and calculate quantity
     * @param pageable
     * @return
     */
    Page<TagsDto> list(Pageable pageable);

    /**
     * add Tags
     * @param tags
     * @return
     */
    Tags add(Tags tags);

    /**
     * update tags by Id
     * @param id
     * @return
     */
    Tags update(int id,Tags tagsUpdate);

    /**
     * delete tags by id
     * @param id
     */
    void deleteById(int id);

    /**
     * find tags by id
     * @param id
     * @return
     */
    Tags findById(int id);

    List<TagsDto> listAll();
}
