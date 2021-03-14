package org.superdeduper.superdeduper.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.service.WatchedFolderService

@RestController
@RequestMapping("/watchedFolders")
class WatchedFolderController {
    @Autowired
    WatchedFolderService watchedFolderService;

    @GetMapping
    List<WatchedFolder> getAllWatchedFolders() {
        return watchedFolderService.getAllWatchedFolders()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addWatchedFolder(@RequestBody WatchedFolder folder) {
        watchedFolderService.addWatchedFolder(folder.path)
    }

    @DeleteMapping("/{id}")
    void deleteWatchedFolder(@PathVariable String id) {
        watchedFolderService.removeWatchedFolderById(id);
    }
}
