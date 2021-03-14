package org.superdeduper.superdeduper.model

import org.springframework.data.mongodb.core.mapping.Document

@Document("music")
class MP3File extends MusicFile {
    ID3v1Info id3v1Info
}
