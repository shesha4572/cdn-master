package com.shesha4572.cdnmaster.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RedisHash("File")
@Data
@AllArgsConstructor
@Builder
public class FileInfo {

    @Id
    private String fileId;
    private LocalDateTime uploadedOn;
    private String fileName;
    private long size;
    private ArrayList<Chunk> chunkList;
}
