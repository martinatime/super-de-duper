package org.superdeduper.superdeduper.batch.reader

import org.springframework.batch.item.data.MongoItemReader
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.superdeduper.superdeduper.model.TrackedFile

class StageFileChecksumReader extends MongoItemReader<TrackedFile> {
    StageFileChecksumReader(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate
        Map<String, Sort.Direction> sortMap = ["id": Sort.Direction.DESC]
        this.sort = sortMap
        this.targetType = TrackedFile
        this.query = "{\$and: [{stage: \"STAGED\"}, { \$or: [ {removed: null}, {removed:{\$exists: false}}, { \$or: [ {md5Checksum: null}, {md5Checksum:{\$exists: false}}, {sha256Checksum: null}, {sha256Checksum:{\$exists: false}} ] } ] }]}"
    }
}
