package com.kakao.openaccount.repository;

import com.kakao.openaccount.domain.TransferCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckWordRepository extends JpaRepository<TransferCheck, Long>{
}
