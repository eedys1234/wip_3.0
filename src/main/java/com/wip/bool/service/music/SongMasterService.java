package com.wip.bool.service.music;

import com.wip.bool.domain.music.SongMaster;
import com.wip.bool.domain.music.SongMasterRepository;
import com.wip.bool.web.dto.music.SongMasterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongMasterService {

    private final SongMasterRepository songMasterRepository;

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
}
