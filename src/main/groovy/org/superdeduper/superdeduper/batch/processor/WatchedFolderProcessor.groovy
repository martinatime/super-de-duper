package org.superdeduper.superdeduper.batch.processor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.superdeduper.superdeduper.model.MusicFile
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.service.FileService
import org.superdeduper.superdeduper.service.MusicFileService

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime

class WatchedFolderProcessor implements ItemProcessor<WatchedFolder, WatchedFolder> {
    static final Logger log = LoggerFactory.getLogger(WatchedFolderProcessor.class);

    @Autowired
    FileService fileService

    @Autowired
    MusicFileService musicFileService

    List<MusicFile> musicFiles = []

    @Override
    WatchedFolder process(@NonNull WatchedFolder item) throws Exception {
        Path watchedFolderPath = Paths.get(item.path)
        File folder = fileService.getFileByPath(watchedFolderPath)
        FileTime fileTime = FileTime.fromMillis(folder.lastModified())
        item.updated = fileTime <=> item.lastUpdate
        if (fileService.isDirectory(folder)) {
            List<File> subFolders = processDirectory(item, folder)
            while(!subFolders.isEmpty()) {
                List<File> working = subFolders.clone()
                subFolders.clear()
                working.each {
                    subFolders.addAll(processDirectory(item, it))
                }
            }
        }
        item.musicFiles = musicFiles
        return item
    }

    private List<File> processDirectory(WatchedFolder item, File directory) {
        log.debug "processDirectory dir=${directory}"
        List<File> subFolders = new ArrayList<>()
        fileService.getChildren(directory).each {
            if (fileService.isDirectory(it)) {
                subFolders.add(it)
            } else {
                processFile(item, it)
            }
        }
        return subFolders
    }

    private void processFile(WatchedFolder item, File file) {
        log.debug "processFile ${file.path}"
        MusicFile musicFile = musicFileService.populateMusicFile(item, file)
        musicFiles << musicFile
    }
}