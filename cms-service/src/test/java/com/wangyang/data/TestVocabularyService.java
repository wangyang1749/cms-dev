package com.wangyang.data;

import com.wangyang.model.pojo.entity.Vocabulary;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

@Transactional
public class TestVocabularyService extends AbstractServiceTest {

    @Test
    public void testAdd(){
        Vocabulary vocabulary = new Vocabulary();

        vocabulary.setWord("treatment");
        vocabulary.setExplains("治疗");
        vocabularyService.add(vocabulary);
    }
}
