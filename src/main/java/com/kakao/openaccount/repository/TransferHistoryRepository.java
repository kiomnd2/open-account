package com.kakao.openaccount.repository;

import com.kakao.openaccount.domain.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    TransferHistory findByUserUUIDAndTransferUUID(String userUUID, String transferUUID);
}
