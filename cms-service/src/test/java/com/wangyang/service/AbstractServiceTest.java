package com.wangyang.service;

import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.IVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(locations = {"classpath:application.properties"})
@SpringBootTest(classes = TestApplication.class)
public abstract class AbstractServiceTest {

    @Autowired
    IVocabularyService vocabularyService;

    @Autowired
    IHtmlService htmlService;
}
