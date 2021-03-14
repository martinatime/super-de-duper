package org.superdeduper.superdeduper.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.nio.file.attribute.FileTime

@Document("music")
class MusicFile {
    @Id
    String id

    String parentPath
    String fullPath
    String[] relativePathParts = []

    MusicFileType type
    FileTime lastUpdated
}