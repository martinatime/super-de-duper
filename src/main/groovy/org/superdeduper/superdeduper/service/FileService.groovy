package org.superdeduper.superdeduper.service

import org.springframework.stereotype.Service
import org.superdeduper.superdeduper.exception.FileNotDirectoryException
import org.superdeduper.superdeduper.exception.FileUnreadableException

import java.nio.file.Path

@Service
class FileService {
    File getFile(String path) {
        File file = new File(path)
        if (canRead(file)) {
            return file
        }
        throw new FileUnreadableException()
    }

    File getFileByPath(Path path) {
        File file = path.toFile()
        if (canRead(file)) {
            return file
        }
        throw new FileUnreadableException()
    }
    
    File[] getChildren(File directory) {
        if (!canRead(directory)) {
            throw new FileUnreadableException()
        }
        if (!isDirectory(directory)) {
            throw new FileNotDirectoryException()
        }
        directory.listFiles()
    }

    boolean canRead(File file) {
        file?.canRead()
    }

    boolean canWrite(File file) {
        file?.canWrite()
    }

    boolean isDirectory(File file) {
        file?.isDirectory()
    }
}