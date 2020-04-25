package com.wangyang.data.service.impl;

import com.wangyang.data.repository.VocabularyRepository;
import com.wangyang.data.service.IVocabularyService;
import com.wangyang.model.pojo.entity.Vocabulary;
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
