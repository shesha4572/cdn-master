package com.shesha4572.cdnmaster.repositories;

import com.shesha4572.cdnmaster.entities.SlavePod;
import org.springframework.data.repository.CrudRepository;


public interface SlavePodRedisRepository extends CrudRepository<SlavePod , String> {
}
