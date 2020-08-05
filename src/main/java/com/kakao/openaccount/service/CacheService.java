package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.CacheDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class CacheService {

    ConcurrentHashMap<String, CacheDTO> cache = new ConcurrentHashMap<>();

    public void addCache(String key, CacheDTO cache) {

        this.cache.putIfAbsent(key, cache);
    }

    public CacheDTO getCache(String key) {
        return this.cache.get(key);
    }

    public void removeCache(String key) {
        this.cache.remove(key);
    }

    public boolean hasCached(String key) {
        return this.cache.containsKey(key);
    }


}
