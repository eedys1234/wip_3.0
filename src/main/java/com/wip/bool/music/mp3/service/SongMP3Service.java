package com.wip.bool.music.mp3.service;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.exception.excp.FileException;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.domain.SongMP3Repository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongMP3Service {

    @Value("${spring.mp3.path}")
    private String mp3FilePath;

    private final SongDetailRepository songDetailRepository;

    private final SongMP3Repository songMP3Repository;

    private final UserRepository userRepository;

    @Transactional
    public Long saveSongMP3(Long userId, Long songDetailId, String orgFileName, byte[] mp3File) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_SONG));

        SongMP3 songMP3 = SongMP3.createSongMP3(songDetail, mp3FilePath, orgFileName, mp3File);

        Long id = songMP3Repository.save(songMP3).getId();

        if(!songMP3.createMP3File(mp3FilePath)) {
            throw new FileException(ErrorCode.CREATE_FAIL_FILE);
        }

        if(!songMP3.updateMP3Info(mp3FilePath)) {
            songMP3.deleteMP3File(mp3FilePath);
            throw new FileException(ErrorCode.CREATE_FAIL_FILE);
        }

        return id;
    }

    @Transactional
    public Long deleteSongMP3(Long userId, Long songMP3Id) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongMP3 songMP3 = songMP3Repository.findById(songMP3Id)
            .orElseThrow(() -> new EntityNotFoundException(songMP3Id, ErrorCode.NOT_FOUND_MP3));

        Long resValue = songMP3Repository.delete(songMP3);

        if(!songMP3.deleteMP3File(mp3FilePath)) {
            throw new FileException(ErrorCode.DELETE_FAIL_FILE);
        }

        return resValue;
    }

    @Transactional(readOnly = true)
    public byte[] getFile(Long songMP3Id) {

        SongMP3 songMP3 = songMP3Repository.findById(songMP3Id)
                .orElseThrow(() -> new EntityNotFoundException(songMP3Id, ErrorCode.NOT_FOUND_MP3));

        return songMP3.getFile();
    }

}
