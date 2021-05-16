package com.wip.bool.userbox.service;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.exception.excp.not_found.NotFoundSongException;
import com.wip.bool.exception.excp.not_found.NotFoundUserBoxException;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.domain.UserBoxSong;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import com.wip.bool.userbox.dto.UserBoxSongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserBoxSongService {

    private final UserBoxSongRepository userBoxSongRepository;

    private final UserBoxRepository userBoxRepository;

    private final SongDetailRepository songDetailRepository;

    @Transactional
    public Long save(UserBoxSongDto.UserBoxSongSaveRequest requestDto) {

        UserBox userBox = userBoxRepository.findById(requestDto.getUserBoxId())
                .orElseThrow(() -> new NotFoundUserBoxException(requestDto.getUserBoxId()));

        SongDetail songDetail = songDetailRepository.findById(requestDto.getSongDetailId())
                .orElseThrow(() -> new NotFoundSongException(requestDto.getSongDetailId()));

        UserBoxSong userBoxSong = UserBoxSong.createUserBoxSong(songDetail, userBox);
        return userBoxSongRepository.save(userBoxSong).getId();
    }

    @Transactional(readOnly = true)
    public List<UserBoxSongDto.UserBoxSongResponse> gets(Long userId, String sort, String order, int size, int offset) {

        SortType sortType = SortType.valueOf(sort);
        OrderType orderType = OrderType.valueOf(order);

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException("정렬기준이 옳바르지 않습니다.");
        }

        return userBoxSongRepository.findAll(userId, sortType, orderType, size, offset);
    }

    @Transactional
    public Long delete(Long userBoxSongId) {
        UserBoxSong userBoxSong = userBoxSongRepository.findById(userBoxSongId)
                .orElseThrow(() -> new NotFoundSongException(userBoxSongId));

        return userBoxSongRepository.delete(userBoxSong);
    }
}
