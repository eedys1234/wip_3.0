package com.wip.bool.music.guitar.service;

import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuitarCodeService {

    private final GuitarCodeRepository guitarCodeRepository;

    @Transactional
    public Long saveGuitarCode(GuitarCodeDto.GuitarCodeSaveRequest requestDto) {

        int order = guitarCodeRepository.maxOrder();
        GuitarCode guitarCode = GuitarCode.createGuitarCode(requestDto.getCode(), order + 1);
        return guitarCodeRepository.save(guitarCode).getId();
    }

    @Transactional(readOnly = true)
    public List<GuitarCodeDto.GuitarCodeResponse> getGuitarCodes() {
        return guitarCodeRepository.findAll()
                .stream()
                .map(GuitarCodeDto.GuitarCodeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long deleteGuitarCode(Long guitarCodeId) {
        GuitarCode guitarCode = guitarCodeRepository.findById(guitarCodeId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 기타코드가 존재하지 않습니다. id = " + guitarCodeId));
        return guitarCodeRepository.delete(guitarCode);
    }

}
