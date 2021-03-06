package com.wip.bool.music.mp3.domain;

import com.wip.bool.cmmn.util.BaseEntity;
import com.wip.bool.cmmn.file.FileManager;
import com.wip.bool.cmmn.file.FileNIOManager;
import com.wip.bool.cmmn.retry.Retry;
import com.wip.bool.cmmn.type.FileExtType;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.song.domain.SongDetail;
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

    @Column(name = "mp3_duration")
    private int duration;

    @Column(name = "mp3_mili_duration")
    private int miliDuration;

    @Column(name = "mp3_file_path", length = 150, nullable = false)
    private String mp3FilePath;

    @Column(name = "mp3_org_file_name", length = 50)
    private String mp3OrgFileName;

    @Column(name = "mp3_new_file_name", length = 32, nullable = false)
    private String mp3NewFileName;

    @Column(name = "mp3_size", nullable = false)
    private int size;

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

    public static SongMP3 createSongMP3(SongDetail songDetail, String mp3FilePath, String orgFileName, byte[] mp3File) {

        SongMP3 songMP3 = new SongMP3();
        songMP3.updateNewFileName();
        songMP3.updateSongDetail(songDetail);
        songMP3.updateMP3FileExt(orgFileName);
        songMP3.updateOrgFileName(orgFileName);
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
                log.error("{} [?????? ?????? ??????] : {}", count, createDirectory(this.mp3NewFileName) + mp3FileExt);
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
                log.error("{} [?????? ?????? ??????] : {}", count, createDirectory(this.mp3NewFileName) + mp3FileExt);
                retry.sleep(count * 100);
            }
        }

        return false;
    }

    private String createDirectory(String fileName) {
        return String.join("/", String.valueOf(fileName.charAt(0)), String.valueOf(fileName.charAt(1)), String.valueOf(fileName.charAt(2)), fileName);
    }

    public void updateNewFileName() {
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

    public void updateOrgFileName(String orgFileName) {
        this.mp3OrgFileName = orgFileName;
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
                log.error("mp3 ????????? ????????? ???????????? ???????????????. ");
                return false;
            }

        return false;
    }

    public byte[] getFile() {

        byte [] bytes = null;
        Path path = null;
        try {
            path = FileSystems.getDefault().getPath(this.mp3FilePath, createDirectory(this.mp3NewFileName) + this.mp3FileExt);

            if(path.toFile().exists()) {
                bytes = Files.readAllBytes(path);
            }
        } catch (IOException e) {
            log.error("mp3 ????????? ???????????? ???????????????. path = " + path.toString());
            throw new EntityNotFoundException("mp3 ????????? ???????????? ???????????????.", ErrorCode.NOT_FOUND_MP3);
        }

        return bytes;
     }
}
