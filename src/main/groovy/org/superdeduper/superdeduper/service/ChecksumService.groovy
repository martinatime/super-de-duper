package org.superdeduper.superdeduper.service

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

import java.security.MessageDigest

@Service
@Slf4j
class ChecksumService {
    String calculateMD5(File file) {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5")
        getFileChecksum(messageDigest, file)
    }

    String calculateSHA256(File file) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256")
        getFileChecksum(messageDigest, file)
    }

    private String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}
