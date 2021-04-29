package com.wip.bool.service.music.guitar;

import com.wip.bool.domain.music.guitar.GuitarCode;
import com.wip.bool.domain.music.guitar.GuitarCodeRepository;
import com.wip.bool.web.dto.music.guitar.GuitarCodeDto;
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
