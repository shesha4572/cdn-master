package com.shesha4572.cdnmaster.repositories;


import com.shesha4572.cdnmaster.entities.Chunk;
import org.springframework.data.repository.CrudRepository;


public interface ChunkRedisRepository extends CrudRepository<Chunk, String> {
}
