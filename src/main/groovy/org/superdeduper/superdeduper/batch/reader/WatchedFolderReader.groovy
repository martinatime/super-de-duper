package org.superdeduper.superdeduper.batch.reader

import org.springframework.batch.item.data.MongoItemReader
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.superdeduper.superdeduper.model.WatchedFolder

class WatchedFolderReader extends MongoItemReader<WatchedFolder> {
    WatchedFolderReader(MongoTemplate mongoTemplate) {
        this.setTemplate(mongoTemplate)
        Map<String, Sort.Direction> sortMap = ["_id": Sort.Direction.DESC]
        this.setSort(sortMap)
        this.targetType = WatchedFolder.class
        this.setQuery("{}")
    }
}
