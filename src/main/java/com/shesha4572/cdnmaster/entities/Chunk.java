package com.shesha4572.cdnmaster.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@RedisHash("Chunks")
@Builder
public class Chunk {
    @Id
    private String chunkId;
    @Indexed
    private String fileId;
    private int chunkIndex;
    private ArrayList<String> replicaPodList;
}
