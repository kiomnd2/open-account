package com.kakao.openaccount.word;


import com.kakao.openaccount.domain.Words;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;



    @PostConstruct
    private void initWordData() throws IOException {
        if (wordRepository.count() == 0 ) { //단어 데이터가 없을 때, 채워넣음
            Resource resource = new ClassPathResource("word_kr.csv");
            List<Words> collect = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .map(line -> Words.builder().wordName(line).build()).collect(Collectors.toList());

            wordRepository.saveAll(collect);
        }

    }
}
