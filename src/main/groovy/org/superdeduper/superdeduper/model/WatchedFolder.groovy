package org.superdeduper.superdeduper.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

import java.nio.file.attribute.FileTime

@Document
class WatchedFolder {
    @Id
    String id

    String path

    FileTime lastUpdate

    @Transient
    List<MusicFile> musicFiles = []

    @Transient
    boolean updated
}
