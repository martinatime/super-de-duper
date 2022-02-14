package org.superdeduper.superdeduper.service

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.superdeduper.superdeduper.model.Stage
import org.superdeduper.superdeduper.model.TrackedFile
import org.superdeduper.superdeduper.repository.TrackedFileRepository

import java.time.Instant

@Service
@Slf4j
class TrackFileService {
    private final TrackedFileRepository trackedFileRepository

    TrackFileService(TrackedFileRepository trackedFileRepository) {
        this.trackedFileRepository = trackedFileRepository
    }

    TrackedFile addTrackedFile(File file, Stage stage, String changeGrouping) {
        TrackedFile trackedFile = new TrackedFile(directory: file.getParent(), fileName: file.getName(), added: Instant.now(), stage: stage, changeGrouping: changeGrouping);
        List<TrackedFile> existing = trackedFileRepository.findByDirectoryAndFileName(file.getParent(), file.getName())
        if (existing.empty) {
            log.debug("Adding new tracked file {} with stage {}", trackedFile.fileName, stage)
            trackedFileRepository.save(trackedFile)
        } else {
            log.debug("Already records for that file")
            for (tf in existing) {
                if (!tf.removed) {
                    log.debug("Not adding new tracked file {} as it already exists", tf.fileName)
                    return tf
                }
            }
            log.debug("None of the existing records are active so adding")
            trackedFileRepository.save(trackedFile)
        }
    }

    TrackedFile removeTrackedFile(File file) {
        List<TrackedFile> existing = trackedFileRepository.findByDirectoryAndFileName(file.getParent(), file.getName())
        if (!existing.empty) {
            log.debug("Removing tracked file {}", file.name)
            for (tf in existing) {
                if (!tf.removed) {
                    tf.removed = Instant.now()
                    trackedFileRepository.save(tf)
                    return tf
                }
            }
        }
        log.debug("{} is not tracked", file.name)
        null
    }
}