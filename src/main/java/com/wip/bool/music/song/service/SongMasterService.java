package com.wip.bool.music.song.service;

import com.wip.bool.cmmn.CodeMapper;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.music.song.dto.SongMasterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongMasterService {

    private final SongMasterRepository songMasterRepository;

    private final CodeMapper codeMapper;

    private final String SONG_MASTER = "song_master";

    @Transactional
    public Long save(SongMasterDto.SongMasterSaveRequest requestDto) {

        Long codeOrder = songMasterRepository.findAllCount();

        SongMaster songMaster = SongMaster.createSongMaster(requestDto.getCodeName(), codeOrder.intValue() + 1);

        return songMasterRepository.save(songMaster).getId();
    }

    @Transactional
    public Long delete(Long songMasterId) {

        SongMaster songMaster = songMasterRepository.findById(songMasterId)
                .orElseThrow(() -> new EntityNotFoundException(songMasterId, ErrorCode.NOT_FOUND_SONG_MASTER));

        return songMasterRepository.delete(songMaster);
    }

    @Transactional(readOnly = true)
    public List<SongMasterDto.SongMasterResponse> gets() {
        return songMasterRepository.findAll().stream()
                .map(SongMasterDto.SongMasterResponse::new)
                .collect(Collectors.toList());
    }
}
