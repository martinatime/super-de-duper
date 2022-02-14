package org.superdeduper.superdeduper.batch.writer

import org.springframework.batch.item.data.MongoItemWriter
import org.superdeduper.superdeduper.model.TrackedFile

class StageFileWriter extends MongoItemWriter<TrackedFile> {
}
