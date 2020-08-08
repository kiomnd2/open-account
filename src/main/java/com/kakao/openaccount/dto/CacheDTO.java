package com.kakao.openaccount.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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
