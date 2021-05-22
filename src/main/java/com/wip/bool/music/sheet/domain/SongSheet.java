package com.wip.bool.music.sheet.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.file.FileManager;
import com.wip.bool.cmmn.file.FileNIOManager;
import com.wip.bool.cmmn.retry.Retry;
import com.wip.bool.cmmn.type.FileExtType;
import com.wip.bool.music.song.domain.SongDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_sheet")
@Slf4j
public class SongSheet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_sheet_id")
    private Long id;

    @Column(name = "sheet_file_path", length = 50, nullable = false, unique = true)
    private String sheetFilePath;

    @Column(name = "sheet_org_file_name", length = 20, nullable = false)
    private String sheetOrgFileName;

    @Column(name = "sheet_new_file_name", length = 32, nullable = false)
    private String sheetNewFileName;

    @Column(name = "sheet_order", nullable = false, unique = true)
    private Integer sheetOrder;

    private Integer size;

    @Column(name = "sheet_file_ext", length = 6, nullable = false)
    private String sheetFileExt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id", nullable = false)
    private SongDetail songDetail;

    @Transient
    private static final int MAX = 5;

    @Transient
    private FileExtType extType;

    @Transient
    private byte[] imagesFile;

    public static SongSheet createSongSheet(SongDetail songDetail, String filePath, String orgFileName, byte[] imagesFile, int sheetOrder) {
        SongSheet songSheet = new SongSheet();
        songSheet.updateSheetOrder(sheetOrder);
        songSheet.updateSongDetail(songDetail);
        songSheet.updateNewFileName();
        songSheet.updateOrgFileName(orgFileName);
        songSheet.updateFileExt(orgFileName);
        songSheet.updateImagesFile(imagesFile);
        songSheet.updateFilePath(filePath);
        return songSheet;
    }

    public boolean createSheetFile(String imagesFilePath) {

        int count = 0;
        Retry retry = new Retry();

        while(count++ < MAX) {
            try {
                    return FileManager.use(imagesFilePath, fileDirectory(this.sheetNewFileName) + sheetFileExt,
                            FileNIOManager.class,
                            fileManager -> fileManager.write(this.imagesFile));

            } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error("{} [파일 생성 실패] : {}/{}", count, imagesFilePath,
                        fileDirectory(this.sheetNewFileName) + sheetFileExt);
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    public boolean deleteSheetFile(String imagesFilePath) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX){
            try {
                return FileManager.delete(imagesFilePath, fileDirectory(this.sheetNewFileName) + sheetFileExt);
            } catch (IOException e) {
                log.error("{} [파일 삭제 실패] : {}", count,
                        fileDirectory(this.sheetNewFileName) + sheetFileExt);
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    private String fileDirectory(String fileName) {
        return String.join("/", String.valueOf(fileName.charAt(0)), String.valueOf(fileName.charAt(1)), String.valueOf(fileName.charAt(2)), fileName);
    }

    public void updateSheetOrder(int sheetOrder) {
        this.sheetOrder = sheetOrder;
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
        songDetail.getSongSheets().add(this);
    }

    public void updateNewFileName() {
        this.sheetNewFileName = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public void updateOrgFileName(String sheetOrgFileName) {
        this.sheetOrgFileName = sheetOrgFileName;
    }

    public void updateFileExt(String orgFileName) {
        this.extType = FileExtType.valueOf(orgFileName.substring(orgFileName.lastIndexOf('.') + 1)
            .toUpperCase());
        this.sheetFileExt = extType.getValue();
    }

    public void updateImagesFile(byte[] imagesFile) {
        this.imagesFile = imagesFile;
        this.size = imagesFile.length;
    }

    public void updateFilePath(String filePath) {
        this.sheetFilePath = filePath;
    }
}
