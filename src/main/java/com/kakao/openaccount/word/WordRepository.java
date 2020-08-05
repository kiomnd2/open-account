package com.kakao.openaccount.word;

import com.kakao.openaccount.domain.Words;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Words, Long> {
}
