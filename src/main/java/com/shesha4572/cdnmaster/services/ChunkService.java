package com.shesha4572.cdnmaster.services;


import com.shesha4572.cdnmaster.entities.SlavePod;
import com.shesha4572.cdnmaster.repositories.SlavePodRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChunkService {

    private final SlavePodRedisRepository slavePodRedisRepository;
    private final int replicationFactor;

    @Autowired
    public ChunkService(SlavePodRedisRepository slavePodRedisRepository){
        this.slavePodRedisRepository = slavePodRedisRepository;
        this.replicationFactor = Integer.parseInt(System.getenv("REPLICATION_FACTOR"));
    }

    public double heuristicValue(SlavePod slavePod){
        if(Math.max(slavePod.getActualChunkLoad().doubleValue() , slavePod.getEstimatedChunkLoad().doubleValue()) >= 1){
            log.warn("SlavePod " + slavePod.getPodName() + " seems to be overloaded. Writes wont be performed on it.");
            return Double.MIN_VALUE;
        }
        int a = 100;
        double x = Math.max(slavePod.getEstimatedChunkLoad().doubleValue() , slavePod.getActualChunkLoad().doubleValue()) + 0.0001;
        double b = 0.5;
        int y = Duration.between(slavePod.getLastAllocatedTimeStamp() , LocalDateTime.now()).toSecondsPart();
        int n = 2;
        double m = 0.5;
        int z = Duration.between(slavePod.getLastPinged() , LocalDateTime.now()).toSecondsPart();
        double o = 2;
        int c = 1;
        double heuristicValue = a * Math.pow(x , -n) + b * Math.pow(y , m) + c * Math.pow(z , o);
        log.info("Slave node " + slavePod.getPodName() + " heuristic value = " + heuristicValue);
        return heuristicValue;

    }

    public ArrayList<String> pickSlaveNode(){
        ArrayList<SlavePod> allPods = (ArrayList<SlavePod>) slavePodRedisRepository.findAll();
        allPods.sort((pod1 , pod2) -> (int) (heuristicValue(pod2) - heuristicValue(pod1)));
        List<SlavePod> chosenPods = allPods.subList(0 , replicationFactor);
        ArrayList<String> chosenPodStrings = new ArrayList<>();
        chosenPods.forEach(pod -> {pod.setEstimatedChunkLoad(pod.getEstimatedChunkLoad().add(BigDecimal.valueOf(0.03))); pod.setLastAllocatedTimeStamp(LocalDateTime.now()); slavePodRedisRepository.save(pod); chosenPodStrings.add(pod.getPodName());});
        return chosenPodStrings;
    }
}