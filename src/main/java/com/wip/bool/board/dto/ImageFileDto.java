package com.wip.bool.board.dto;

import com.wip.bool.board.domain.ImageFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageFileDto {

    @Getter
    @NoArgsConstructor
    public static class ImageFileResponse {

        private String filePath;

        private String newFileName;

        private String fileExt;

        public ImageFileResponse(ImageFile imageFile) {
            this.filePath = imageFile.getFilePath();
            this.newFileName = imageFile.getNewFileName();
            this.fileExt = imageFile.getImageFileExt();
        }
    }

}
