package com.shesha4572.cdnmaster.services;


import com.shesha4572.cdnmaster.entities.Chunk;
import com.shesha4572.cdnmaster.entities.SlavePod;
import com.shesha4572.cdnmaster.repositories.ChunkRedisRepository;
import com.shesha4572.cdnmaster.repositories.SlavePodRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class HeartBeatService {

    private final SlavePodRedisRepository slavePodRedisRepository;
    private final ChunkRedisRepository chunkRedisRepository;

    @Autowired
    public HeartBeatService(SlavePodRedisRepository slavePodRedisRepository, ChunkRedisRepository chunkRedisRepository){
        this.slavePodRedisRepository = slavePodRedisRepository;
        this.chunkRedisRepository = chunkRedisRepository;
    }

    public void heartBeatRegister(String podName , BigDecimal chunkLoad , ArrayList<String> newChunks){
        SlavePod slavePod;
        if(slavePodRedisRepository.existsById(podName)){
            log.info("Slave " + podName + " pinged. Load : " + chunkLoad + " , " + newChunks.size() + " new Chunk replica(s)");
            slavePod = slavePodRedisRepository.findById(podName).get();
            slavePod.setLastPinged(LocalDateTime.now());
            slavePod.setActualChunkLoad(chunkLoad);
            slavePod.setEstimatedChunkLoad(chunkLoad);
        }
        else {
            log.info("Slave " + podName + " discovered. Load : " + chunkLoad + " , " + newChunks.size() + " new Chunk replica(s)");
            slavePod = SlavePod.builder()
                    .actualChunkLoad(chunkLoad)
                    .estimatedChunkLoad(chunkLoad)
                    .lastAllocatedTimeStamp(LocalDateTime.MIN)
                    .lastPinged(LocalDateTime.now())
                    .podName(podName)
                    .build();
        }
        slavePodRedisRepository.save(slavePod);
        setChunkReplicas(podName , newChunks);
    }

    public void setChunkReplicas(String podName , ArrayList<String> newChunks){
        newChunks.forEach(
                newChunk -> {
                    Chunk chunk = chunkRedisRepository.findById(newChunk).get();
                    if(chunk.getReplicaPodList() == null){
                        ArrayList<String> replicaList = new ArrayList<>();
                        replicaList.add(podName);
                        chunk.setReplicaPodList(replicaList);
                        chunkRedisRepository.save(chunk);
                    }
                    else if (!chunk.getReplicaPodList().contains(podName)){
                        ArrayList<String> replicaList = chunk.getReplicaPodList();
                        replicaList.add(podName);
                        chunk.setReplicaPodList(replicaList);
                        chunkRedisRepository.save(chunk);
                    }
                }
        );
    }

}
