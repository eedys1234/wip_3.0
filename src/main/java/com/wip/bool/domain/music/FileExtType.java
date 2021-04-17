package com.wip.bool.domain.music;

import lombok.Getter;

@Getter
public enum FileExtType {
    PNG(".PNG"),
    JPG(".JPG"),
    GIF(".GIF"),
    MP3(".MP3"),
    MP4(".MP4");

    private String value;

    FileExtType(String value) {
        this.value = value;
    }
}
