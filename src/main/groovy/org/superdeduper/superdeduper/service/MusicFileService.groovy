package org.superdeduper.superdeduper.service

import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.superdeduper.superdeduper.model.*
import org.superdeduper.superdeduper.repository.MusicFileRepository

import java.nio.file.Files
import java.nio.file.Path

@Slf4j
@Service
class MusicFileService {

    @Autowired
    MusicFileRepository musicFileRepository

    MusicFile populateMusicFile(WatchedFolder item, File file) {
        Path filePath = file.toPath()
        String contentType = Files.probeContentType(filePath);
        MusicFileType musicFileType = MusicFileType.fromMimeType(contentType);
        MusicFile musicFile = new MusicFile(fullPath:filePath.toString(),type:musicFileType, parentPath: item.path, lastUpdated: Files.getLastModifiedTime(filePath))
//        switch (musicFileType) {
//            case MusicFileType.MP3:
//                return populateMP3File(musicFile)
//                break
//            case MusicFileType.M4A:
//                return populateM4AFile(musicFile)
//                break
//            case MusicFileType.OGG:
//                return populateOGGFile(musicFile)
//                break
//            case MusicFileType.WAV:
//                return populateWAVFile(musicFile)
//                break
//            default:
//                return null
//                break
//        }
    }

    MP3File populateMP3File(MusicFile musicFile) {
        (MP3File)musicFile
    }

    M4AFile populateM4AFile(MusicFile musicFile) {
    }

    OGGFile populateOGGFile(MusicFile musicFile) {

    }

    WAVFile populateWAVFile(MusicFile musicFile) {

    }

    void addMusicFile(MusicFile musicFile) {
        Optional<MusicFile> musicFileOptional = musicFileRepository.findByFullPath(musicFile.fullPath)
        if (!musicFileOptional.present) {
            log.debug "inserting ${musicFile}"
            musicFileRepository.insert(musicFile)
        }
    }

    void updateMusicFile(MusicFile musicFile) {
        Optional<MusicFile> musicFileOptional = musicFileRepository.findById(musicFile.id)
        if (musicFileOptional.present) {
            log.debug "saving ${musicFile}"
            musicFileRepository.save(musicFile)
        }
    }

    void deleteMusicFile(String id) {
        Optional<MusicFile> musicFileOptional = musicFileRepository.findById(id)
        if (musicFileOptional.present) {
            musicFileRepository.deleteById(id)
        }
    }

    MusicFile findById(String id) {
        Optional<MusicFile> musicFileOptional = musicFileRepository.findById(id)
        if (musicFileOptional.present) {
            return musicFileOptional.get()
        }
        return null
    }

    MusicFile findByPath(String path) {
        Optional<MusicFile> musicFileOptional = musicFileRepository.findByFullPath(path)
        if (musicFileOptional.present) {
            return musicFileOptional.get()
        }
        return null
    }
}