package com.kakao.openaccount.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CacheDTO {

    private String requestUserUUID;

    private String transferUUID;

    private LocalDateTime cachingDate;

    private StateType type = StateType.NORMAL;

}
