package com.shesha4572.cdnmaster.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RedisHash("SlavePod")
@Builder
public class SlavePod {
    @Id
    private String podName;
    private BigDecimal actualChunkLoad;
    private BigDecimal estimatedChunkLoad;
    private LocalDateTime lastAllocatedTimeStamp;
    private LocalDateTime lastPinged;
}
