package com.shesha4572.cdnmaster.controllers;


import com.shesha4572.cdnmaster.models.HeartBeatDto;
import com.shesha4572.cdnmaster.services.HeartBeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/heartbeat")
@RequiredArgsConstructor
public class HeartBeatController {

    private final HeartBeatService heartBeatService;

    @PutMapping("/")
    public ResponseEntity<?> heartBeat(@RequestBody HeartBeatDto heartBeatDetails){
        heartBeatService.heartBeatRegister(heartBeatDetails.getPodName() , BigDecimal.valueOf(heartBeatDetails.getChunkLoad()), heartBeatDetails.getNewChunks());
        return ResponseEntity.ok().build();
    }
}
