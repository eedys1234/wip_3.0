package com.wip.bool.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.board.domain.ImageFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageFileDto {

    @Getter
    @NoArgsConstructor
    public static class ImageFileResponse {

        @JsonProperty(value = "file_path")
        private String filePath;

        @JsonProperty(value = "new_file_name")
        private String newFileName;

        @JsonProperty(value = "file_ext")
        private String fileExt;

        public ImageFileResponse(ImageFile imageFile) {
            this.filePath = imageFile.getFilePath();
            this.newFileName = imageFile.getNewFileName();
            this.fileExt = imageFile.getImageFileExt();
        }
    }

}
