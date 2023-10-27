package com.shesha4572.cdnmaster.repositories;


import com.shesha4572.cdnmaster.entities.FileInfo;
import org.springframework.data.repository.CrudRepository;

public interface FileRedisRepository extends CrudRepository<FileInfo, String> {
}
