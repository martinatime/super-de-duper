package org.superdeduper.superdeduper.model

import org.springframework.data.annotation.Id

import java.time.Instant

class TrackedFile {
    @Id
    BigInteger id
    String directory
    String fileName
    Instant added
    Instant removed
    Stage stage
    String changeGrouping
    String md5Checksum
    String sha256Checksum
}
