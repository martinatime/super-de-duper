package org.superdeduper.superdeduper.batch.writer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.superdeduper.superdeduper.model.MusicFile
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.service.MusicFileService
import org.superdeduper.superdeduper.service.WatchedFolderService

class WatchedFolderWriter implements ItemWriter<WatchedFolder> {
    static final Logger log = LoggerFactory.getLogger(WatchedFolderWriter.class);
    @Autowired
    WatchedFolderService watchedFolderService

    @Autowired
    MusicFileService musicFileService

    @Override
    void write(List<? extends WatchedFolder> items) throws Exception {
       items?.each {
           log.debug "Saving music files under ${it.path}"
           it.musicFiles?.each {
               musicFileService.addMusicFile(it)
           }
           if (it.updated) {
               //watchedFolderService.
           }
       }
    }
}
