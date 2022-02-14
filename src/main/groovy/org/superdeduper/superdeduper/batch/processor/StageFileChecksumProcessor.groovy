package org.superdeduper.superdeduper.batch.processor

import groovy.util.logging.Slf4j
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.superdeduper.superdeduper.model.TrackedFile
import org.superdeduper.superdeduper.service.ChecksumService

import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class StageFileChecksumProcessor implements ItemProcessor<TrackedFile, TrackedFile> {

    @Autowired
    private final ChecksumService checksumService;

    @Override
    TrackedFile process(@NonNull TrackedFile item) throws Exception {
        Path trackedFilePath = Paths.get(item.directory, item.fileName)
        File trackedFile = trackedFilePath.toFile()
        if (!item.md5Checksum) {
            item.md5Checksum = checksumService.calculateMD5(trackedFile)
        }

        if (!item.sha256Checksum) {
            item.sha256Checksum = checksumService.calculateSHA256(trackedFile)
        }

        return item
    }
}
