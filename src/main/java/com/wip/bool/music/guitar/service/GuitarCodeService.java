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
    public Long save(GuitarCodeDto.GuitarCodeSaveRequest requestDto) {

        int order = guitarCodeRepository.maxOrder();
        GuitarCode guitarCode = GuitarCode.createGuitarCode(requestDto.getCode(), order + 1);
        return guitarCodeRepository.save(guitarCode).getId();
    }

    @Transactional(readOnly = true)
    public List<GuitarCodeDto.GuitarCodeResponse> gets() {
        return guitarCodeRepository.findAll()
                .stream()
                .map(GuitarCodeDto.GuitarCodeResponse::new)
                .collect(Collectors.toList());
    }

}
