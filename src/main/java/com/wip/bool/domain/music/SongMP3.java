package com.wip.bool.domain.music;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.cmmn.file.FileManager;
import com.wip.bool.domain.cmmn.retry.Retry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "song_mp3")
@Slf4j
public class SongMP3 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_mp3_id")
    private Long id;

    private int duration;

    private int miliDuration;

    @Column(name = "mp3_path")
    private String mp3Path;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @Transient
    private final int MAX = 5;

    @Transient
    private FileExtType extType = FileExtType.MP4;

    public static SongMP3 createSongMP3(SongDetail songDetail) {

        SongMP3 songMP3 = new SongMP3();
        songMP3.updateMP3Path();
        songMP3.updateSongDetail(songDetail);
        return songMP3;
    }

    public boolean createMP3File(String filePath, byte[] mp3Files) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX) {

            try {
                return FileManager.use(filePath, this.mp3Path + extType.getValue(),
                        fileManager -> fileManager.write(mp3Files));
            }
            catch (IOException e) {
                log.error("%d [파일 생성 실패] : %s", count, FileManager.getsFileDirectory(this.mp3Path + extType.getValue()));
                retry.sleep(count * 100);
            }
        }
        return false;
    }

    public boolean deleteMP3File(String filePath) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX) {
            try {
                return FileManager.delete(filePath, this.mp3Path + extType.getValue());
            }catch (IOException e) {
                log.error("%d [파일 삭제 실패] : %s", count, FileManager.getsFileDirectory(this.mp3Path + extType.getValue()));
                retry.sleep(count * 100);

            }
        }

        return false;
    }

    public void updateMP3Path() {
        this.mp3Path = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
    }

    public boolean updateMP3Info(String filePath) {

        File file = new File(filePath + this.mp3Path + extType.getValue());
        if(file.exists() && file.isFile()) {

            try{

                MP3File mp3 = (MP3File)AudioFileIO.read(file);
                this.duration = mp3.getAudioHeader().getTrackLength();
                this.miliDuration = duration * 1000;
                return true;
            } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                log.error(String.format("mp3 파일의 정보를 가져오지 못했습니다. "));
                return false;
            }
        }

        return false;
    }

    public byte[] getFile(String filePath) {

        byte [] bytes = null;

        try {
            bytes = Files.readAllBytes(Paths.get(filePath + this.mp3Path + extType.getValue()));
        } catch (IOException e) {
            log.error(String.format("mp3 파일을 가져오지 못했습니다."));
        }

        return bytes;
     }
}
