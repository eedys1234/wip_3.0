package com.wip.bool.domain.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.domain.music.OrderType;
import com.wip.bool.domain.music.SortType;
import com.wip.bool.web.dto.user.BookMarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wip.bool.domain.music.QSongDetail.songDetail;
import static com.wip.bool.domain.music.QGuitarCode.guitarCode;
import static com.wip.bool.domain.user.QBookMark.bookMark;


@Repository
@RequiredArgsConstructor
public class BookMarkRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public BookMark save(BookMark bookMark) {
        entityManager.persist(bookMark);
        return bookMark;
    }

    // TODO : userId에 index 추가, 드라이빙 테이블은 무엇이 좋을까?? Order By(TITLE, GuitarCoe)는 어떻게??
    // TODO : 드라이빙 테이블은 song-detail, 드리븐 테이블은 book-mark
    // TODO : bookmark 테이블의 index는 songDetailId, userId, song-detail 테이블의 index는 title, guitarcode
    public List<BookMarkDto.BookMarkResponse> findAll(Long userId, SortType sortType, OrderType orderType, int offset, int size) {
        return queryFactory.select(Projections.constructor(BookMarkDto.BookMarkResponse.class,
                bookMark.id, songDetail.id, songDetail.title, bookMark.createDate))
                .from(songDetail)
                .innerJoin(bookMark.songDetail)
                .fetchJoin()
                .innerJoin(songDetail.guitarCode, guitarCode)
                .fetchJoin()
                .where(bookMark.user.id.eq(userId))
                .offset(offset)
                .limit(size)
                .orderBy(getOrder(sortType, orderType))
                .fetch();

    }

    private OrderSpecifier getOrder(SortType sortType, OrderType orderType) {
        if(SortType.TITLE.equals(sortType)) {
            return titleOrder(orderType);
        }
        return guitarCodeOrder(orderType);
    }

    private OrderSpecifier titleOrder(OrderType orderType) {
        return OrderType.ASC.equals(orderType) ? songDetail.title.asc()
                : songDetail.title.desc();
    }

    private OrderSpecifier guitarCodeOrder(OrderType orderType) {
        return OrderType.ASC.equals(orderType) ? songDetail.guitarCode.guitarOrder.asc() :
                songDetail.guitarCode.guitarOrder.desc();
    }

}
