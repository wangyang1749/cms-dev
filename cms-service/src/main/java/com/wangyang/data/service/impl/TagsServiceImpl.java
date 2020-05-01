package com.wangyang.data.service.impl;

import com.wangyang.data.service.ITagsService;
import com.wangyang.model.pojo.dto.TagsDto;
import com.wangyang.model.pojo.entity.Tags;
import com.wangyang.data.repository.TagsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        Optional<Tags> tagsOptional = findBy(tags.getName());
        if(tagsOptional.isPresent()){
            return tagsOptional.get();
        }
        log.info("添加 Tags"+tags.getName());
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


    @Override
    public Optional<Tags> findBy(String tagName){
        return  Optional.ofNullable(tagsRepository.findTagsByName(tagName));
    }

    @Override
    public Optional<Tags> findBySlugName(String slugName){
        return  Optional.ofNullable(tagsRepository.findTagsBySlugName(slugName));
    }

}
