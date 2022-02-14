package org.superdeduper.superdeduper.batch.processor

import groovy.util.logging.Slf4j
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.superdeduper.superdeduper.config.FileWatcherConfig
import org.superdeduper.superdeduper.model.TrackedFile
import org.superdeduper.superdeduper.service.TrackFileService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Slf4j
class StageFileProcessor implements ItemProcessor<TrackedFile, TrackedFile> {

    @Autowired
    private final FileWatcherConfig fileWatcherConfig

    @Autowired
    private final TrackFileService trackFileService

    @Override
    TrackedFile process(@NonNull TrackedFile item) throws Exception {
        Path watchedFolder = Paths.get(fileWatcherConfig.watchedFolder)
        Path stagingFolder = Paths.get(fileWatcherConfig.stagingFolder)

        Path filePath = Paths.get(item.directory, item.fileName)
        Path relative = watchedFolder.relativize(filePath)

        Path newFilePath = stagingFolder.resolve(relative)
        Path baseNewFilePath = newFilePath.parent
        int i = 0
        while (Files.exists(newFilePath)) {
            i++
            log.debug("New file path already exists {}", newFilePath)
            newFilePath = baseNewFilePath.resolve("${i}").resolve(newFilePath.fileName)
            log.debug("Trying {}", newFilePath)
        }
        Files.createDirectories(newFilePath.parent)

        log.debug("Moving {} to {} ", filePath, newFilePath)
        Files.move(filePath, newFilePath, StandardCopyOption.ATOMIC_MOVE)

        Path directory = filePath.parent
        while (directory != watchedFolder) {
            if (isEmpty(directory)) {
                log.debug("Deleting empty directory {}", directory)
                Files.delete(directory)
                directory = directory.parent
            } else {
                break
            }
        }
        return item
    }

    private boolean isEmpty(Path directory) {
        if (Files.isDirectory(directory)) {
            try (Stream<Path> entries = Files.list(directory)) {
                return !entries.findFirst().isPresent()
            }
        }
    }
}
