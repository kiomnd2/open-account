package com.kakao.openaccount.service;

import com.kakao.openaccount.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WordServiceTest {

    @Autowired
    WordService wordService;

    @Autowired
    WordRepository wordRepository;


    @Test
    public void getRandomNumberAndConfirm() {
        long randomSequence = wordService.findRandomSequence();

        String word = wordService.getWord(randomSequence);

        assertNotNull(word);


    }

}
