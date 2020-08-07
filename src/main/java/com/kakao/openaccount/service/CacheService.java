package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.CacheDTO;
import com.kakao.openaccount.dto.TransferRequestDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class CacheService {

    ConcurrentHashMap<String, CacheDTO> cache = new ConcurrentHashMap<>();

    protected void addCache(String key, CacheDTO cache) {

        this.cache.putIfAbsent(key, cache);
    }

    public CacheDTO getCache(String transferUUID) {
        return this.cache.get(transferUUID);
    }

    public int getCacheSize() {
        return cache.size();
    }

    public void removeCache(String key) {
        this.cache.remove(key);
    }

    public boolean hasCached(String key) {
        return this.cache.containsKey(key);
    }

    public void saveCache(TransferRequestDTO requestDTO) {
        CacheDTO cache = CacheDTO.builder()
                .cachingDate(LocalDateTime.now())
                .requestUserUUID(requestDTO.getRequestUserUUID())
                .transferUUID(requestDTO.getTransferUUID()).build();
        this.addCache(requestDTO.getTransferUUID(), cache);
    }

    public void clear() {
        this.cache.clear();
    }
}

