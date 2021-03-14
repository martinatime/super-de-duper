package org.superdeduper.superdeduper.model

enum MusicFileType {
    MP3 ("audio/mpeg"),
    OGG ("audio/ogg"),
    M4A ("audio/mp4"),
    WAV ("audio/wav")

    String mimeType;

    private MusicFileType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static MusicFileType fromMimeType(String mimeType) {
        for (MusicFileType musicFileType in values()) {
            if (musicFileType.mimeType.equalsIgnoreCase(mimeType)) {
                return musicFileType
            }
        }
        return null
    }
}