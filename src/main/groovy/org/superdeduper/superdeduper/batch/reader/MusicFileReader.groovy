package org.superdeduper.superdeduper.batch.reader

import org.springframework.batch.item.data.MongoItemReader
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.superdeduper.superdeduper.model.MusicFile

class MusicFileReader extends MongoItemReader<MusicFile> {
    MusicFileReader(MongoTemplate mongoTemplate) {
        this.setTemplate(mongoTemplate)
        Map<String, Sort.Direction> sortMap = ["_id": Sort.Direction.DESC]
        this.setSort(sortMap)
        this.targetType = MusicFile.class
        this.setQuery("{}")
    }
}
