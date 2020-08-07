package com.kakao.openaccount.service;


import com.kakao.openaccount.domain.Word;
import com.kakao.openaccount.exception.service.NotFoundWordException;
import com.kakao.openaccount.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public long findRandomSequence() {
        long count = wordRepository.count();
        return (long) (Math.random() * count) + 1;
    }

    public boolean isCorrectInputWord(long i, String input) {
        return input.equals(getWord(i));
    }

    public String getWord(long i) {
        return wordRepository.getOne(i).getWordName();
    }


    @PostConstruct
    private void initWordData() throws IOException {
        if (wordRepository.count() == 0 ) { //단어 데이터가 없을 때, 채워넣음
            Resource resource = new ClassPathResource("word_kr.csv");
            List<Word> collect = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .map(line -> Word.builder().wordName(line).build()).collect(Collectors.toList());

            wordRepository.saveAll(collect);
        }
    }

}
