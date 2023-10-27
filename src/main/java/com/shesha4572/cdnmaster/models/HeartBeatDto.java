package com.shesha4572.cdnmaster.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Data
@Builder
public class HeartBeatDto {
    String podName;
    Double chunkLoad;
    ArrayList<String> newChunks;
}
