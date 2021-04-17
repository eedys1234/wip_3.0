package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.cmmn.file.FileManager;
import com.wip.bool.domain.cmmn.retry.Retry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;

@Getter
@NoArgsConstructor
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

    public boolean deleteSheetFile(String filePath) {

        boolean isDelete = false;
        final int MAX = 5;
        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX){
            try {
                isDelete = FileManager.delete(filePath, this.sheetPath);
                return isDelete;
            } catch (IOException e) {
                log.error("%d [파일 삭제 실패] : %s", count, FileManager.getsFileDirectory(this.sheetPath));
                retry.sleep(100 * count);
            }
        }

        return false;
    }
}
