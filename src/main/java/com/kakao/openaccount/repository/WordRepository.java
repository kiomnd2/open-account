package com.kakao.openaccount.repository;

import com.kakao.openaccount.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
