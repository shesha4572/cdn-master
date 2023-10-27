package com.shesha4572.cdnmaster.controllers;


import com.shesha4572.cdnmaster.entities.FileInfo;
import com.shesha4572.cdnmaster.models.CreateFileDto;
import com.shesha4572.cdnmaster.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RequestMapping("/api/v1/file")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/createFile")
    public ResponseEntity<?> createFile(@RequestBody CreateFileDto fileDetails){
        FileInfo fileInfo = FileInfo.builder()
                .fileId(fileDetails.getFileId())
                .fileName(fileDetails.getFileName())
                .size(fileDetails.getFileSizeBytes())
                .build();
        try {
            ArrayList<ArrayList<String>> outputAllocation = fileService.allocateChunks(fileInfo);
            return ResponseEntity.ok().body(outputAllocation);
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
