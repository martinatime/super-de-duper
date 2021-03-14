package org.superdeduper.superdeduper.batch.processor

import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.Mp3File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.superdeduper.superdeduper.model.ID3v1Info
import org.superdeduper.superdeduper.model.MP3File
import org.superdeduper.superdeduper.model.MusicFile
import org.superdeduper.superdeduper.model.MusicFileType
import org.superdeduper.superdeduper.service.FileService

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.util.stream.Collectors

class MusicFilePopulationProcessor implements ItemProcessor<MusicFile, MusicFile> {
    static final Logger log = LoggerFactory.getLogger(MusicFilePopulationProcessor.class);

    @Autowired
    FileService fileService

    @Override
    MusicFile process(@NonNull MusicFile item) throws Exception {
        log.debug "process ${item}"
        File file = fileService.getFile(item.fullPath)
        FileTime currentFileTime = FileTime.fromMillis(file.lastModified())
        if (currentFileTime != item.lastUpdated || !item.relativePathParts) {
            item.relativePathParts = calculateRelativePathParts(item.parentPath, item.fullPath)

            if (item.type == MusicFileType.MP3) {
                MP3File mp3File = convertToMp3(item)

                Mp3File mp3 = new Mp3File(item.fullPath)
                if (mp3.hasId3v1Tag()) {
                    mp3File.id3v1Info = buildID3v1Info(mp3.id3v1Tag)
                }
                item = mp3File
            }
            return item
        }
        return null
    }

    private MP3File convertToMp3(MusicFile musicFile) {
        MP3File mp3File = new MP3File()
        mp3File.with {
            id = musicFile.id
            parentPath = musicFile.parentPath
            fullPath = musicFile.fullPath
            relativePathParts = musicFile.relativePathParts
            type = musicFile.type
            lastUpdated = musicFile.lastUpdated
        }
        return mp3File
    }

    private List<String> calculateRelativePathParts(String parentPath, String fullPath) {
        List<String> result = []
        Path parent = Paths.get(parentPath)
        Path child = Paths.get(fullPath)
        Path relative = parent.relativize(child)
        Iterator<Path> iterator = relative.iterator()
        while(iterator.hasNext()) {
            result << iterator.next().toString()
        }

        return result
    }

    private ID3v1Info buildID3v1Info(ID3v1 id3v1) {
        ID3v1Info id3v1Info = new ID3v1Info()
        id3v1Info.with {
            album = id3v1.album
            artist = id3v1.artist
            comment = id3v1.comment
            genre = id3v1.genreDescription
            title = id3v1.title
            track = id3v1.track
            version = id3v1.version
            year = id3v1.year
        }

        return  id3v1Info
    }
}
