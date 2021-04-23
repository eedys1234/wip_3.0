package com.wip.bool.domain.music;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.web.dto.music.SongDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SongDetailRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public SongDetail save(SongDetail songDetail) {
        entityManager.persist(songDetail);
        return songDetail;
    }

    public List<SongDetailDto.SongDetailResponse> findAll(SongMaster songMaster, SortType sortType,
                                         OrderType order, int offset, int size) {

        return queryFactory.select(
                Projections.constructor(SongDetailDto.SongDetailResponse.class,
                QSongDetail.songDetail.id, QSongDetail.songDetail.title))
                .from(QSongDetail.songDetail)
                .where(songMasterEq(songMaster))
                .offset(offset)
                .limit(size)
                .orderBy(getOrder(sortType, order))
                .fetch();
    }

    public List<String> findAll() {
        return queryFactory.select(QSongDetail.songDetail.title)
                .from(QSongDetail.songDetail)
                .fetch();
    }

    public Long delete(SongDetail songDetail) {
        entityManager.remove(songDetail);
        return 1L;
    }

    public Optional<SongDetail> findById(Long songDetailId) {
        //TODO : BookMark, GuitarCode와 JOIN 해야함
        return Optional.ofNullable(entityManager.find(SongDetail.class, songDetailId));

//        return queryFactory.select(QSongDetail.songDetail)
//                .where(QSongDetail.songDetail.id.eq(songDetailId))
//                .fetchOne();
    }

    //SongMaster
    private BooleanExpression songMasterEq(SongMaster songMaster) {
        return songMaster != null ? QSongDetail.songDetail.songMaster.eq(songMaster) : null;
    }

    private OrderSpecifier getOrder(SortType sortType, OrderType order) {

        if(SortType.TITLE.equals(sortType)) {
            return titleOrder(order);
        }
        return guitarCodeOrder(order);
    }

    private OrderSpecifier titleOrder(OrderType order) {
        return OrderType.ASC.equals(order) ? QSongDetail.songDetail.title.asc() :
                QSongDetail.songDetail.title.desc();
    }

    private OrderSpecifier guitarCodeOrder(OrderType order) {
        return OrderType.ASC.equals(order) ? QSongDetail.songDetail.guitarCode.guitarOrder.asc() :
                QSongDetail.songDetail.guitarCode.guitarOrder.desc();
    }

}
