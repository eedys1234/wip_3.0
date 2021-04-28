package com.wip.bool.domain.music.mp3;

import com.wip.bool.cmmn.BaseEntity;
import com.wip.bool.cmmn.file.FileManager;
import com.wip.bool.cmmn.file.FileNIOManager;
import com.wip.bool.cmmn.retry.Retry;
import com.wip.bool.cmmn.type.FileExtType;
import com.wip.bool.domain.music.song.SongDetail;
import com.wip.bool.exception.excp.NotFoundFileException;
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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Column(name = "mp3_file_path", length = 50, nullable = false)
    private String mp3FilePath;

    @Column(name = "mp3_org_file_name", length = 20, nullable = false)
    private String mp3OrgFileName;

    @Column(name = "mp3_new_file_name", length = 32, nullable = false)
    private String mp3NewFileName;

    @Column(nullable = false)
    private long size;

    @Column(name = "mp3_file_ext", length = 5, nullable = false)
    private String mp3FileExt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_detail_id")
    private SongDetail songDetail;

    @Transient
    private static final int MAX = 5;

    @Transient
    private FileExtType extType;

    @Transient
    private byte[] mp3File;

    public static SongMP3 createSongMP3(SongDetail songDetail, String mp3FilePath, String mp3OrgFileName, byte[] mp3File) {

        SongMP3 songMP3 = new SongMP3();
        songMP3.updateMP3Path();
        songMP3.updateSongDetail(songDetail);
        songMP3.updateMP3FileExt(mp3OrgFileName);
        songMP3.updateOrgFileName(mp3OrgFileName);
        songMP3.updateMP3File(mp3File);
        songMP3.updateMP3FilePath(mp3FilePath);
        return songMP3;
    }

    public boolean createMP3File(String filePath) {

        int count = 1;
        Retry retry = new Retry();

        while(count++ <= MAX) {

            try {
                return FileManager.use(filePath, createDirectory(this.mp3NewFileName) + mp3FileExt,
                        FileNIOManager.class,
                        fileManager -> fileManager.write(mp3File));
            }
            catch (IOException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                log.error("{} [파일 생성 실패] : {}", count, createDirectory(this.mp3NewFileName) + mp3FileExt);
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
                return FileManager.delete(filePath, createDirectory(this.mp3NewFileName) + mp3FileExt);
            } catch (IOException e) {
                log.error("{} [파일 삭제 실패] : {}", count, createDirectory(this.mp3NewFileName) + mp3FileExt);
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    private String createDirectory(String fileName) {
        return String.join("/", String.valueOf(fileName.charAt(0)), String.valueOf(fileName.charAt(1)), String.valueOf(fileName.charAt(2)), fileName);
    }

    public void updateMP3Path() {
        this.mp3NewFileName = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public void updateSongDetail(SongDetail songDetail) {
        this.songDetail = songDetail;
    }

    public void updateMP3FileExt(String mp3OrgFileName) {
        this.extType = FileExtType.valueOf(mp3OrgFileName.substring(mp3OrgFileName.lastIndexOf('.') + 1)
        .toUpperCase());
        this.mp3FileExt = extType.getValue();
    }

    public void updateOrgFileName(String mp3OrgFileName) {
        this.mp3OrgFileName = mp3OrgFileName;
    }

    public void updateMP3File(byte[] mp3File) {
        this.mp3File = mp3File;
        this.size = mp3File.length;
    }

    public void updateMP3FilePath(String mp3FilePath) {
        this.mp3FilePath = mp3FilePath;
    }

    public boolean updateMP3Info(String filePath) {

        try {
                Path path = FileSystems.getDefault().getPath(filePath, createDirectory(this.mp3NewFileName) + mp3FileExt);
                File file = path.toFile();

                if(file.exists()) {

                    MP3File mp3 = (MP3File) AudioFileIO.read(file);
                    this.duration = mp3.getAudioHeader().getTrackLength();
                    this.miliDuration = duration * 1000;
                    return true;
                }

            } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                log.error("mp3 파일의 정보를 가져오지 못했습니다. ");
                return false;
            }

        return false;
    }

    public byte[] getFile(String filePath) {

        byte [] bytes = null;

        try {
            Path path = FileSystems.getDefault().getPath(filePath, createDirectory(this.mp3NewFileName) + mp3FileExt);
            if(path.toFile().exists()) {
                bytes = Files.readAllBytes(path);
            }
        } catch (IOException e) {
            log.error("mp3 파일을 가져오지 못했습니다.");
            throw new NotFoundFileException("mp3 파일을 가져오지 못했습니다.");
        }

        return bytes;
     }
}
