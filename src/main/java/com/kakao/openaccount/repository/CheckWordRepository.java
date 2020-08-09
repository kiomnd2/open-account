package com.kakao.openaccount.repository;

import com.kakao.openaccount.domain.TransferCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckWordRepository extends JpaRepository<TransferCheck, Long>{
    TransferCheck findByTransferUUID(String transferUUID);
    List<TransferCheck> findByUserUUID(String userUUID);
    TransferCheck findByUserUUIDAndTransferUUID(String userUUID, String transferUUID);
}
