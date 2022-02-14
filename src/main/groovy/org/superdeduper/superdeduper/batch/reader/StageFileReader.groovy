package org.superdeduper.superdeduper.batch.reader

import org.springframework.batch.item.data.MongoItemReader
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.superdeduper.superdeduper.model.TrackedFile

class StageFileReader extends MongoItemReader<TrackedFile> {
    StageFileReader(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate
        Map<String, Sort.Direction> sortMap = ["id": Sort.Direction.DESC]
        this.sort = sortMap
        this.targetType = TrackedFile
        this.query = "{\$and: [{stage: \"INTAKE\"}, { \$or: [ {removed: null}, {removed:{\$exists: false}} ] } ] }"
    }
}
