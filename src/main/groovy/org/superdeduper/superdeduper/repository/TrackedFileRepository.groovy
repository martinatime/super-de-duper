package org.superdeduper.superdeduper.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.superdeduper.superdeduper.model.TrackedFile

interface TrackedFileRepository extends MongoRepository<TrackedFile, String> {
    List<TrackedFile> findByDirectoryAndFileName(String directory, String fileName)
}