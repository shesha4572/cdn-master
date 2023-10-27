package com.shesha4572.cdnmaster.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;


@Data
@Builder
public class CreateFileDto {
    String fileName;
    String fileId;
    long fileSizeBytes;
}
