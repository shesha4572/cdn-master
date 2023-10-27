package com.shesha4572.cdnmaster.repositories;


import com.shesha4572.cdnmaster.entities.Chunk;
import com.shesha4572.cdnmaster.entities.SlavePod;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ChunkRedisRepository extends CrudRepository<Chunk, String> {
    Chunk getChunkByChunkId(String chunkId);
}
