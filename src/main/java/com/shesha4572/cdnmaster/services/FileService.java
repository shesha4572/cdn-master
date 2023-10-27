package com.shesha4572.cdnmaster.services;


import com.shesha4572.cdnmaster.entities.Chunk;
import com.shesha4572.cdnmaster.entities.FileInfo;
import com.shesha4572.cdnmaster.repositories.ChunkRedisRepository;
import com.shesha4572.cdnmaster.repositories.FileRedisRepository;
import com.shesha4572.cdnmaster.repositories.SlavePodRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class FileService {

    private final FileRedisRepository fileRedisRepository;
    private final ChunkRedisRepository chunkRedisRepository;
    private final ChunkService chunkService;
    private final SlavePodRedisRepository slavePodRedisRepository;
    private final int replicationFactor;

    @Autowired
    public FileService(FileRedisRepository fileRedisRepository , ChunkService chunkService , ChunkRedisRepository chunkRedisRepository , SlavePodRedisRepository slavePodRedisRepository){
        this.fileRedisRepository = fileRedisRepository;
        this.chunkService = chunkService;
        this.chunkRedisRepository = chunkRedisRepository;
        this.slavePodRedisRepository = slavePodRedisRepository;
        this.replicationFactor = Integer.parseInt(System.getenv("REPLICATION_FACTOR"));
    }

    public ArrayList<ArrayList<String>> allocateChunks(FileInfo file) throws RuntimeException{
        int noOfChunks = (int) (file.getSize() / 64000000);
        int lastChunkSize = (int) (file.getSize() % 64000000);
        if(lastChunkSize > 0) noOfChunks += 1;
        log.info("File " + file.getFileId() + " with " + noOfChunks + " chunk(s) is being allocated");
        if(slavePodRedisRepository.count() < replicationFactor){
            log.warn("File " + file.getFileId() + " allocation deferred due to insufficient slave node count");
            throw new RuntimeException("Not enough slave nodes to satisfy replication factor");
        }
        ArrayList<ArrayList<String>> chunkAllocations = new ArrayList<>(noOfChunks);
        ArrayList<Chunk> chunkArrayList = new ArrayList<>();
        for (int i = 0; i < noOfChunks; i++) {
            Chunk chunk = Chunk.builder()
                    .chunkId(RandomStringUtils.randomAlphanumeric(16))
                    .chunkIndex(i)
                    .fileId(file.getFileId())
                    .build();
            ArrayList<String> chosenPods = chunkService.pickSlaveNode();
            chosenPods.add(0 , chunk.getChunkId());
            chunkAllocations.add(chosenPods);
            chunkArrayList.add(chunk);
            chunkRedisRepository.save(chunk);
            log.info("Chunk #" + chunk.getChunkIndex() + " " + chunk.getChunkId() + " assigned to slaves " + chosenPods.subList(1,chosenPods.size()));
        }
        file.setChunkList(chunkArrayList);
        fileRedisRepository.save(file);
        return chunkAllocations;
    }


}
