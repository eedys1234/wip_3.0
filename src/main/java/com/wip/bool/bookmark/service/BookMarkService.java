package com.wip.bool.bookmark.service;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.bookmark.domain.BookMarkRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.bookmark.dto.BookMarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final SongDetailRepository songDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userId, BookMarkDto.BookMarkSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        SongDetail songDetail = songDetailRepository.findById(requestDto.getSongDetailId())
                .orElseThrow(() -> new IllegalArgumentException("해당 곡이 존재하지 않습니다. id = " + requestDto.getSongDetailId()));

        BookMark bookMark = BookMark.createBookMark(user, songDetail);

        return bookMarkRepository.save(bookMark).getId();
    }

    @Transactional
    public Long delete(Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 존재하지 않습니다. id = " + bookMarkId));
        return bookMarkRepository.delete(bookMark);
    }

    @Transactional(readOnly = true)
    public List<BookMarkDto.BookMarkResponse> gets(Long userId, String sort, String order, int size, int offset) {

        SortType sortType = SortType.valueOf(sort);
        OrderType orderType = OrderType.valueOf(order);

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException("정렬기준이 옳바르지 않습니다.");
        }

        return bookMarkRepository.findAll(userId, sortType, orderType, size, offset);
    }
}
