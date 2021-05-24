package com.wip.bool.cmmn.bookmark;

import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookMarkFactory {

    public static List<BookMark> getBookMarksWithId(User user, List<SongDetail> songDetails) {
        AtomicInteger index = new AtomicInteger(0);
        return songDetails.stream()
                .map(songDetail -> getBookMark(user, songDetail, index.incrementAndGet()))
                .collect(Collectors.toList());
    }


    public static List<BookMark> getBookMarks(User user, List<SongDetail> songDetails) {
        return songDetails.stream()
                            .map(songDetail -> getBookMark(user, songDetail))
                            .collect(Collectors.toList());
    }

    public static BookMark getBookMark(User user, SongDetail songDetail) {
        BookMark bookMark = BookMark.createBookMark(user, songDetail);
        return bookMark;
    }

    public static BookMark getBookMark(User user, SongDetail songDetail, long id) {
        BookMark bookMark = getBookMark(user, songDetail);
        ReflectionTestUtils.setField(bookMark, "id", id);
        return bookMark;
    }

}
