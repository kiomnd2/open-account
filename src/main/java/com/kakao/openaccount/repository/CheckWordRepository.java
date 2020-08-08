package com.kakao.openaccount.repository;

import com.kakao.openaccount.domain.TransferCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckWordRepository extends JpaRepository<TransferCheck, Long>{
    TransferCheck findByTransferUUID(String transferUUID);
    TransferCheck findByUserUUID(String userUUID);
}
