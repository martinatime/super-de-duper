package org.superdeduper.superdeduper.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.superdeduper.superdeduper.model.MusicFile

interface MusicFileRepository extends MongoRepository<MusicFile, String> {
    Optional<MusicFile> findByFullPath(String fullPath)
}