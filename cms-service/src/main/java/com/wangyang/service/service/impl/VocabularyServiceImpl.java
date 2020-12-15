package com.wangyang.service.service.impl;

import com.wangyang.service.repository.VocabularyRepository;
import com.wangyang.service.service.IVocabularyService;
import com.wangyang.pojo.entity.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VocabularyServiceImpl implements IVocabularyService {

    @Autowired
    VocabularyRepository vocabularyRepository;

    @Override
    public Vocabulary add(Vocabulary vocabulary){
        return vocabularyRepository.save(vocabulary);
    }
}
