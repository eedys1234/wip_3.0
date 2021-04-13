package com.wip.bool.domain.music;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public QueryResults<SongDetail> findAll(SongMaster songMaster, String sortType,
                                            String order, PageRequest pageRequest) {

        return queryFactory.selectFrom(QSongDetail.songDetail)
                .where(songMasterEq(songMaster))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getOrder(sortType, order))
                .fetchResults();
    }

    public Long delete(SongDetail songDetail) {
        entityManager.remove(songDetail);
        return 1L;
    }

    public Optional<SongDetail> findById(Long songDetailId) {
        return Optional.ofNullable(entityManager.find(SongDetail.class, songDetailId));
    }

    //SongMaster
    private BooleanExpression songMasterEq(SongMaster songMaster) {
        return songMaster != null ? QSongDetail.songDetail.songMaster.eq(songMaster) : null;
    }

    private OrderSpecifier getOrder(String sortType, String order) {

        if("TITLE".equals(sortType)) {
            return titleOrder(order);
        }
        return guitarCodeOrder(order);
    }

    private OrderSpecifier titleOrder(String order) {
        return "ASC".equals(order) ? QSongDetail.songDetail.title.asc() :
                QSongDetail.songDetail.title.desc();
    }

    private OrderSpecifier guitarCodeOrder(String order) {
        return "ASC".equals(order) ? QSongDetail.songDetail.guitarCode.guitarOrder.asc() :
                QSongDetail.songDetail.guitarCode.guitarOrder.desc();
    }

}
