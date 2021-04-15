package com.wip.bool.service.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongDetailService {

    private final SongDetailRepository songDetailRepository;
    private final SongMasterRepository songMasterRepository;
    private final GuitarCodeRepository guitarCodeRepository;
    private final WordsMasterRepository wordsMasterRepository;

    @Transactional
    public Long save(SongDto.SongDetailSaveRequest requestDto) {

        SongMaster songMaster = songMasterRepository.findById(requestDto.getCodeKey())
                .orElseThrow(() -> new IllegalArgumentException("code key가 존재하지 않습니다. "));

        GuitarCode guitarCode = guitarCodeRepository.findById(requestDto.getGuitarCodeKey())
                .orElseThrow(() -> new IllegalArgumentException("guitar code id가 존재하지 않습니다. "));

        WordsMaster wordsMaster = wordsMasterRepository.findById(requestDto.getWordsMasterKey())
                .orElseThrow(() -> new IllegalArgumentException("words master id가 존재하지 않습니다. "));

        SongDetail songDetail = SongDetail.createSongDetail(requestDto.getTitle(), requestDto.getLyrics(), songMaster,
                guitarCode, wordsMaster);

        return songDetailRepository.save(songDetail).getId();
    }

}
