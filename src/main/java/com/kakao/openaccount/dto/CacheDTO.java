package com.kakao.openaccount.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
public class CacheDTO {

    @NotNull
    @Max(40)
    private String requestUserUUID;

    @NotNull
    @Max(40)
    private String transferUUID;

    @DateTimeFormat
    private LocalDateTime cachingDate;
}
