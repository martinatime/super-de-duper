package org.superdeduper.superdeduper.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.superdeduper.superdeduper.model.WatchedFolder

interface WatchedFolderRepository extends MongoRepository<WatchedFolder, String> {
    Optional<WatchedFolder> findByPath(String path)
}
