package com.wip.bool.recent.service;

import com.wip.bool.exception.excp.not_found.NotFoundUserException;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.recent.domain.Recent;
import com.wip.bool.recent.domain.RecentRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.recent.dto.RecentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentService {

    private final RecentRepository recentRepository;
    private final UserRepository userRepository;
    private final SongDetailRepository songDetailRepository;

    @Transactional
    public Long save(Long userId, RecentDto.RecentSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        SongDetail songDetail = songDetailRepository.findById(requestDto.getSongDetailId())
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + requestDto.getSongDetailId()));


        Recent recent = Recent.createRecent(songDetail, user);

        return recentRepository.save(recent).getId();
    }

    @Transactional(readOnly = true)
    public List<RecentDto.RecentResponse> gets(Long userId, int size, int offset) {
        return recentRepository.findAll(userId, size, offset);
    }
}
