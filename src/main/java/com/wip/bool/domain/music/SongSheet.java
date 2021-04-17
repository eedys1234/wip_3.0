package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.cmmn.file.FileManager;
import com.wip.bool.domain.cmmn.retry.Retry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_sheet")
@Slf4j
public class SongSheet extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sheet_path")
    private String sheetPath;

    @Column(name = "sheet_order")
    private int sheetOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @Transient
    private final int MAX = 5;

    @Transient
    private FileExtType extType = FileExtType.PNG;

    public static SongSheet createSongSheet(SongDetail songDetail, int sheetOrder) {
        SongSheet songSheet = new SongSheet();
        songSheet.updateSheetOrder(sheetOrder);
        songSheet.updateSongDetail(songDetail);
        songSheet.updateSheetPath();
        return songSheet;
    }

    public boolean createSheetFile(String filePath, byte[] imageFiles) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX) {
            try {
                    return FileManager.use(filePath, this.sheetPath + extType.getValue(), fileManager -> {
                        fileManager.write(imageFiles); });

            } catch (IOException e) {
                log.error("%d [파일 생성 실패] : %s", count,
                        FileManager.getsFileDirectory(this.sheetPath + extType.getValue()));
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    public boolean deleteSheetFile(String filePath) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX){
            try {
                return FileManager.delete(filePath, this.sheetPath + extType.getValue());
            } catch (IOException e) {
                log.error("%d [파일 삭제 실패] : %s", count,
                        FileManager.getsFileDirectory(this.sheetPath + extType.getValue()));
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    public void updateSheetOrder(int sheetOrder) {
        this.sheetOrder = sheetOrder;
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
        songDetail.getSongSheets().add(this);
    }

    public void updateSheetPath() {
        this.sheetPath = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

}
