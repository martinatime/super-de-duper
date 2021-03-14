package org.superdeduper.superdeduper.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.repository.WatchedFolderRepository

import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.LocalDateTime

@Service
class WatchedFolderService {
    @Autowired
    WatchedFolderRepository watchedFolderRepository

    @Autowired
    FileService fileService

    List<WatchedFolder> getAllWatchedFolders() {
        return watchedFolderRepository.findAll()
    }

    void addWatchedFolder(String path) {
        Optional<WatchedFolder> watchedFolderOptional = watchedFolderRepository.findByPath(path)
        if (!watchedFolderOptional.present) {
            File file = fileService.getFile(path)
            if (fileService.isDirectory(file)) {
                WatchedFolder watchedFolder = new WatchedFolder(path: Paths.get(path), lastUpdate: file.lastModified())
                watchedFolderRepository.save(watchedFolder)
            }
        }
    }

    void removeWatchedFolderById(String id) {
        Optional<WatchedFolder> watchedFolderOptional = watchedFolderRepository.findById(id)
        if (watchedFolderOptional.present) {
            watchedFolderRepository.delete(watchedFolderOptional.get())
        }
    }

    void removeWatchedFolder(String path) {
        Optional<WatchedFolder> watchedFolderOptional = watchedFolderRepository.findByPath(path)
        if (watchedFolderOptional.present) {
            watchedFolderRepository.delete(watchedFolderOptional.get())
        }
    }
}
