package org.superdeduper.superdeduper.filewatchlistener


import org.superdeduper.superdeduper.model.Stage
import org.superdeduper.superdeduper.service.TrackFileService

class StagedFolderListener extends AbstractFileListener {
    StagedFolderListener(File watchedFolder, TrackFileService trackFileService) {
        this.trackFileService = trackFileService
        this.watchedFolder = watchedFolder
        this.stage = Stage.STAGED
    }
}
