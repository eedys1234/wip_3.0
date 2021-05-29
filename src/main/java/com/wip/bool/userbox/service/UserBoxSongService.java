package com.wip.bool.userbox.service;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepositoryImpl;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.domain.UserBoxSong;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import com.wip.bool.userbox.dto.UserBoxSongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserBoxSongService {

    private final UserBoxSongRepository userBoxSongRepository;

    private final UserBoxRepository userBoxRepository;

    private final SongDetailRepository songDetailRepository;

    private final RightsRepositoryImpl rightsRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    @Transactional
    public Long saveUserBoxSong(Long userId, Long userBoxId, UserBoxSongDto.UserBoxSongSaveRequest requestDto) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Group> groups = groupRepository.findAllByUser(userId);

        List<Long> authorityIds = groups.stream()
                                        .map(Group::getId)
                                        .collect(Collectors.toList());

        if(Objects.isNull(authorityIds)) {
            authorityIds = new ArrayList<>();
        }

        authorityIds.add(userId);
        authorityIds.add(user.getDept().getId());

        List<Rights> rights = rightsRepository.findByUserBox(userBoxId, authorityIds);

        if(rights.stream().allMatch(right -> right.getRightType() < (Rights.RightType.READ.getValue() | Rights.RightType.WRITE.getValue()))) {
            throw new AuthorizationException();
        }

        UserBox userBox = userBoxRepository.findById(userBoxId)
                .orElseThrow(() -> new EntityNotFoundException(userBoxId, ErrorCode.NOT_FOUND_USER_BOX));

        SongDetail songDetail = songDetailRepository.findById(requestDto.getSongDetailId())
                .orElseThrow(() -> new EntityNotFoundException(requestDto.getSongDetailId(), ErrorCode.NOT_FOUND_SONG));

        UserBoxSong userBoxSong = UserBoxSong.createUserBoxSong(songDetail, userBox);

        return userBoxSongRepository.save(userBoxSong).getId();
    }

    @Transactional(readOnly = true)
    public List<UserBoxSongDto.UserBoxSongResponse> findAllUserBoxSong(Long userId, Long userBoxId, String sort, String order, int size, int offset) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Group> groups = groupRepository.findAllByUser(userId);

        List<Long> authorityIds = groups.stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        if(Objects.isNull(authorityIds)) {
            authorityIds = new ArrayList<>();
        }

        authorityIds.add(userId);
        authorityIds.add(user.getDept().getId());

        List<Rights> rights = rightsRepository.findByUserBox(userBoxId, authorityIds);

        if(rights.stream().anyMatch(right -> right.getRightType() < Rights.RightType.READ.getValue())) {
            throw new AuthorizationException();
        }

        SortType sortType = SortType.valueOf(sort);
        OrderType orderType = OrderType.valueOf(order);

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException("정렬기준이 옳바르지 않습니다.");
        }

        return userBoxSongRepository.findAll(userBoxId, sortType, orderType, size, offset);
    }

    @Transactional
    public Long deleteUserBoxSong(Long userId, Long userBoxId, Long userBoxSongId) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        List<Group> groups = groupRepository.findAllByUser(userId);

        List<Long> authorityIds = groups.stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        if(Objects.isNull(authorityIds)) {
            authorityIds = new ArrayList<>();
        }

        authorityIds.add(userId);
        authorityIds.add(user.getDept().getId());

        List<Rights> rights = rightsRepository.findByUserBox(userBoxId, authorityIds);

        if(rights.stream().allMatch(right -> right.getRightType() < (Rights.RightType.READ.getValue() | Rights.RightType.WRITE.getValue()))) {
            throw new AuthorizationException();
        }

        UserBoxSong userBoxSong = userBoxSongRepository.findById(userBoxSongId)
                .orElseThrow(() -> new EntityNotFoundException(userBoxSongId, ErrorCode.NOT_FOUND_SONG));

        return userBoxSongRepository.delete(userBoxSong);
    }
}
