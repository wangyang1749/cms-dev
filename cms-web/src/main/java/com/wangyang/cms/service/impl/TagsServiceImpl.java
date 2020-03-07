package com.wangyang.cms.service.impl;

import com.wangyang.cms.pojo.dto.TagsDto;
import com.wangyang.cms.pojo.entity.Tags;
import com.wangyang.cms.repository.TagsRepository;
import com.wangyang.cms.service.ITagsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagsServiceImpl implements ITagsService {

    @Autowired
    TagsRepository tagsRepository;
    @Override
    public Page<TagsDto> list(Pageable pageable) {
        Page<Tags> tagsPage = tagsRepository.findAll(pageable);
        return tagsPage.map(tags -> {
           TagsDto tagsDto = new TagsDto();
           BeanUtils.copyProperties(tags,tagsDto);
           return tagsDto;
        });
    }

    @Override
    public List<TagsDto> listAll() {
        List<Tags> tags = tagsRepository.findAll();
        return  tags.stream().map(tag->{
            TagsDto tagsDto = new TagsDto();
            BeanUtils.copyProperties(tag,tagsDto);
            return tagsDto;
        }).collect(Collectors.toList());
    }
    @Override
    public Tags add(Tags tags) {
        return tagsRepository.save(tags);
    }

    @Override
    public Tags update(int id,Tags tagsUpdate) {
        Tags tags = findById(id);
        BeanUtils.copyProperties(tagsUpdate,tags);
        return  tagsRepository.save(tags);
    }

    @Override
    public void deleteById(int id) {
        tagsRepository.deleteById(id);
    }

    @Override
    public Tags findById(int id) {
        Optional<Tags> optionalTags = tagsRepository.findById(id);
        if(optionalTags.isPresent()){
            return optionalTags.get();
        }
        return null;
    }


}
