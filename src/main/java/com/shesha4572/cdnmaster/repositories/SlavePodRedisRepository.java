package com.shesha4572.cdnmaster.repositories;

import com.shesha4572.cdnmaster.entities.SlavePod;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;


public interface SlavePodRedisRepository extends CrudRepository<SlavePod , String> {
    SlavePod findSlavePodByPodName(String podName);
}
