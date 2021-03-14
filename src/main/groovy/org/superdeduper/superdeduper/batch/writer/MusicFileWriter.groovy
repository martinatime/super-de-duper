package org.superdeduper.superdeduper.batch.writer

import org.springframework.batch.item.data.MongoItemWriter
import org.superdeduper.superdeduper.model.MusicFile

class MusicFileWriter extends MongoItemWriter<MusicFile> {
}
