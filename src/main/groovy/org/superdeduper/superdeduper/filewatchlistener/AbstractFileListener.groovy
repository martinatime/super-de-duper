package org.superdeduper.superdeduper.filewatchlistener

import groovy.util.logging.Slf4j
import org.springframework.boot.devtools.filewatch.ChangedFile
import org.springframework.boot.devtools.filewatch.ChangedFiles
import org.springframework.boot.devtools.filewatch.FileChangeListener
import org.superdeduper.superdeduper.model.Stage
import org.superdeduper.superdeduper.service.TrackFileService

import java.time.LocalDate

@Slf4j
abstract class AbstractFileListener implements FileChangeListener {
    protected File watchedFolder
    protected Stage stage
    protected TrackFileService trackFileService

    @Override
    void onChange(Set<ChangedFiles> changeSet) {
        LocalDate localDate = LocalDate.now()
        long salt = System.currentTimeMillis() % 1789
        def changeGrouping = "${localDate}-${salt}"
        for (ChangedFiles cFiles : changeSet) {
            if (!cFiles.sourceDirectory.path.startsWith(watchedFolder.path)) {
                log.debug("Filtering out changes({}) in other watched directories {}", cFiles.sourceDirectory.path, watchedFolder.path);
                continue
            }

            for (ChangedFile cFile : cFiles.getFiles()) {
                log.info("Event happened " + cFile.getType() + ":" + cFile.getFile().getName())
                switch (cFile.type) {
                    case ChangedFile.Type.ADD:
                        handleAdd(cFile, changeGrouping)
                        break
                    case ChangedFile.Type.MODIFY:
                        handleModify(cFile)
                        break
                    case ChangedFile.Type.DELETE:
                        handleDelete(cFile)
                        break
                    default:
                        log.debug("Another action happened {}", cFile.type)
                }
            }
        }
    }

    void handleAdd(ChangedFile changedFile, String changeGrouping) {
        log.debug("ADD {}", changedFile.file)
        trackFileService.addTrackedFile(changedFile.file, stage, changeGrouping)
    }

    void handleModify(ChangedFile changedFile) {
        log.debug("MODIFY {}", changedFile.file)
        //trackFileService.addTrackedFile(changedFile.file, stage)
    }

    void handleDelete(ChangedFile changedFile) {
        log.debug("DELETE {}", changedFile.file)
        trackFileService.removeTrackedFile(changedFile.file)
    }
}
