package com.wip.bool.board.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.file.FileManager;
import com.wip.bool.cmmn.retry.Retry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image_file")
@Slf4j
public class ImageFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_file_id")
    private Long id;

    @Column(name = "image_file_path", length = 150, nullable = false)
    private String filePath;

    @Column(name = "org_file_name", length = 100)
    private String orgFileName;

    @Column(name = "new_file_name", unique = true, length = 32, nullable = false)
    private String newFileName;

    @Column(name = "image_file_size", nullable = false)
    private int size;

    @Column(name = "image_file_ext", length = 5, nullable = false)
    private String imageFileExt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Transient
    private final int MAX = 5;

    public static ImageFile createImageFile(String filePath, String orgFileName) {
        ImageFile imageFile = new ImageFile();
        imageFile.updateFilePath(filePath);
        imageFile.updateOrgFileName(orgFileName);
        imageFile.updateFileExt(orgFileName);
        imageFile.updateNewFileName();
        return imageFile;
    }

    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void updateOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public void updateNewFileName() {
        this.newFileName = UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public void updateSize(int size) {
        this.size = size;
    }

    public void updateFileExt(String orgFileName) {
        this.imageFileExt = orgFileName.substring(orgFileName.lastIndexOf('.') + 1).toUpperCase();
    }

    public void updateBoard(Board board) {
        if(!Objects.isNull(board)) {
            board.getImageFiles().add(this);
            this.board = board;
        }
    }

    public void updateReply(Reply reply) {
        if (!Objects.isNull(reply)) {
            reply.getImageFiles().add(this);
            this.reply = reply;
        }
    }

    public boolean createImageFile(String tempFilePath) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX) {
            try {
                byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath(tempFilePath));
                updateSize(bytes.length);
                boolean isMove = FileManager.move(tempFilePath, String.format("%s/%s", this.filePath, fileDirectory(this.newFileName)));
                return isMove;
            } catch (IOException e) {
                log.error("{} 임시파일을 이동하는데 실패하였습니다. {}", count, tempFilePath);
                retry.sleep(count * 100);
            }
        }
        return false;
    }

    public boolean deleteImageFile() {

        int count = 1;
        Retry retry = new Retry();

        while(count <= MAX) {

            try {
                return FileManager.delete(this.filePath, fileDirectory(this.newFileName));
            } catch (IOException e) {
                log.error("{} 파일 삭제 실패 {}", count, fileDirectory(this.newFileName));
                retry.sleep(100 * count);
            }
        }
        return false;
    }

    private String fileDirectory(String fileName) {
        return String.join("/", String.valueOf(fileName.charAt(0)), String.valueOf(fileName.charAt(1)), String.valueOf(fileName.charAt(2)), fileName);
    }


}
