package com.kakao.openaccount.word;

import com.kakao.openaccount.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
