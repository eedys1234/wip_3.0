package com.wip.bool.service.music.song;

import com.wip.bool.cmmn.CodeMapper;
import com.wip.bool.domain.music.song.SongMaster;
import com.wip.bool.domain.music.song.SongMasterRepository;
import com.wip.bool.web.dto.music.song.SongMasterDto;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 분류가 존재하지 않습니다. id = " + songMasterId));

        return songMasterRepository.delete(songMaster);
    }

    @Transactional(readOnly = true)
    public List<SongMasterDto.SongMasterResponse> gets() {
        return songMasterRepository.findAll().stream()
                .map(SongMasterDto.SongMasterResponse::new)
                .collect(Collectors.toList());
    }
}
