package com.wip.bool.domain.music.song;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.web.dto.music.song.SongDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.domain.music.QSongDetail.songDetail;
import static com.wip.bool.domain.music.QGuitarCode.guitarCode;
import static com.wip.bool.domain.user.QBookMark.bookMark;

@Repository
@RequiredArgsConstructor
public class SongDetailRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public SongDetail save(SongDetail songDetail) {
        entityManager.persist(songDetail);
        return songDetail;
    }

    public List<SongDetailDto.SongDetailSimpleResponse> findAll(SongMaster songMaster, SortType sortType,
                                                                OrderType order, int offset, int size) {

        return queryFactory.select(
                Projections.constructor(SongDetailDto.SongDetailSimpleResponse.class,
                songDetail.id, songDetail.title))
                .from(songDetail)
                .join(songDetail.guitarCode, guitarCode)
                .where(songMasterEq(songMaster))
                .orderBy(getOrder(sortType, order))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<String> findAll() {
        return queryFactory.select(songDetail.title)
                .from(songDetail)
                .fetch();
    }

    public Long delete(SongDetail songDetail) {
        entityManager.remove(songDetail);
        return 1L;
    }

    public Optional<SongDetail> findById(Long songDetailId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(songDetail)
                .fetchOne());
    }

    public Optional<SongDetailDto.SongDetailResponse> findById(Long songDetailId, Long userId) {
        //TODO : BookMark, GuitarCode와 JOIN 해야함

        return Optional.ofNullable(
                queryFactory.select(
                        Projections.constructor(SongDetailDto.SongDetailResponse.class,
                        songDetail, bookMark))
                .from(songDetail)
                .leftJoin(bookMark)
                .on(bookMark.songDetail.eq(songDetail), bookMark.user.id.eq(userId))
                .innerJoin(songDetail.guitarCode, guitarCode)
                .fetchJoin()
                .fetchOne());
    }

    //SongMaster
    private BooleanExpression songMasterEq(SongMaster songMaster) {
        return songMaster != null ? songDetail.songMaster.eq(songMaster) : null;
    }

    private OrderSpecifier getOrder(SortType sortType, OrderType order) {

        if(SortType.TITLE.equals(sortType)) {
            return titleOrder(order);
        }
        return guitarCodeOrder(order);
    }

    private OrderSpecifier titleOrder(OrderType order) {
        return OrderType.ASC.equals(order) ? songDetail.title.asc() :
                songDetail.title.desc();
    }

    private OrderSpecifier guitarCodeOrder(OrderType order) {
        return OrderType.ASC.equals(order) ? songDetail.guitarCode.guitarOrder.asc() :
                songDetail.guitarCode.guitarOrder.desc();
    }

}
