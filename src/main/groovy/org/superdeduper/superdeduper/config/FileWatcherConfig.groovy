package org.superdeduper.superdeduper.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.devtools.filewatch.FileSystemWatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.superdeduper.superdeduper.filewatchlistener.StagedFolderListener
import org.superdeduper.superdeduper.filewatchlistener.WatchedFolderListener
import org.superdeduper.superdeduper.repository.TrackedFileRepository
import org.superdeduper.superdeduper.service.TrackFileService

import javax.annotation.PreDestroy
import java.time.Duration

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "file-watcher")
class FileWatcherConfig {
    String watchedFolder;
    String stagingFolder;
    Long pollInterval;
    Long quietPeriod;

    @Autowired
    private final TrackFileService trackFileService

    @Bean
    FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(pollInterval), Duration.ofMillis(quietPeriod))
        File watchedFolderFile = new File(watchedFolder)
        File stagedFolderFile = new File(stagingFolder)
        def watchedFolders = [watchedFolderFile, stagedFolderFile]
        fileSystemWatcher.addSourceDirectories(watchedFolders)
        fileSystemWatcher.addListener(new WatchedFolderListener(watchedFolderFile, trackFileService))
        fileSystemWatcher.addListener(new StagedFolderListener(stagedFolderFile, trackFileService))
        fileSystemWatcher.start()

        return fileSystemWatcher
    }

    @PreDestroy
    void onDestroy() throws Exception {
        fileSystemWatcher().stop()
    }
}
