package com.shesha4572.cdnmaster.controllers;


import com.shesha4572.cdnmaster.entities.FileInfo;
import com.shesha4572.cdnmaster.models.CreateFileDto;
import com.shesha4572.cdnmaster.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                .uploadedOn(LocalDateTime.now())
                .build();
        try {
            ArrayList<ArrayList<String>> outputAllocation = fileService.allocateChunks(fileInfo);
            return ResponseEntity.ok().body(outputAllocation);
        }
        catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/getAllFileChunks/{fileId}")
    public ResponseEntity<?> getAllFileChunks(@PathVariable String fileId){
        try {
            FileInfo fileInfo = fileService.getFileChunks(fileId);
            return ResponseEntity.ok().body(fileInfo);
        }
        catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

}
