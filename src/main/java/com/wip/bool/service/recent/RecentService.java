package com.wip.bool.service.recent;

import com.wip.bool.domain.music.song.SongDetail;
import com.wip.bool.domain.music.song.SongDetailRepository;
import com.wip.bool.domain.recent.Recent;
import com.wip.bool.domain.recent.RecentRepository;
import com.wip.bool.domain.user.User;
import com.wip.bool.domain.user.UserRepository;
import com.wip.bool.web.dto.recent.RecentDto;
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
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

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
