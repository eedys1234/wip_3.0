package com.wip.bool.music.sheet.service;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.exception.excp.FileException;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongSheetService {

    @Value("${spring.images.path}")
    private String imageFilePath;

    private final SongSheetRepository songSheetRepository;
    private final SongDetailRepository songDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveSongSheet(Long userId, Long songDetailId, String orgFileName, byte[] imageFiles) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongDetail songDetail = songDetailRepository.findById(songDetailId)
                .orElseThrow(() -> new EntityNotFoundException(songDetailId, ErrorCode.NOT_FOUND_SONG));

        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetailId);

        SongSheet songSheet = SongSheet.createSongSheet(songDetail, imageFilePath, orgFileName, imageFiles, songSheets.size() + 1);

        Long id = songSheetRepository.save(songSheet).getId();

        if(!songSheet.createSheetFile(imageFilePath)) {
            throw new FileException(ErrorCode.CREATE_FAIL_FILE);
        }

        return id;
    }

    @Transactional
    public Long deleteSongSheet(Long userId, Long songSheetId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        SongSheet songSheet = songSheetRepository.findById(songSheetId)
            .orElseThrow(() -> new EntityNotFoundException(songSheetId, ErrorCode.NOT_FOUND_SHEET));

        Long resValue = songSheetRepository.delete(songSheet);
        if(!songSheet.deleteSheetFile(imageFilePath)) {
            throw new FileException(ErrorCode.DELETE_FAIL_FILE);
        }

        return resValue;
    }

}
