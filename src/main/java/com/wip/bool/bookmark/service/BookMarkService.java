package com.wip.bool.bookmark.service;

import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.bookmark.domain.BookMarkRepository;
import com.wip.bool.bookmark.dto.BookMarkDto;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
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
    public Long saveBookMark(Long userId, BookMarkDto.BookMarkSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        SongDetail songDetail = songDetailRepository.findById(requestDto.getSongDetailId())
                .orElseThrow(() -> new EntityNotFoundException(requestDto.getSongDetailId(), ErrorCode.NOT_FOUND_SONG));

        BookMark bookMark = BookMark.createBookMark(user, songDetail);

        return bookMarkRepository.save(bookMark).getId();
    }

    @Transactional
    public Long deleteBookMark(Long userId, Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(userId, bookMarkId)
                .orElseThrow(() -> new EntityNotFoundException(bookMarkId, ErrorCode.NOT_FOUND_BOOKMARK));
        return bookMarkRepository.delete(bookMark);
    }

    @Transactional(readOnly = true)
    public List<BookMarkDto.BookMarkResponse> findBookMarks(Long userId, String sort, String order, int size, int offset) {

        SortType sortType = SortType.valueOf(sort);
        OrderType orderType = OrderType.valueOf(order);

        if(Objects.isNull(sortType) || Objects.isNull(orderType)) {
            throw new IllegalArgumentException("정렬기준이 옳바르지 않습니다.");
        }

        return bookMarkRepository.findAll(userId, sortType, orderType, size, offset);
    }
}
