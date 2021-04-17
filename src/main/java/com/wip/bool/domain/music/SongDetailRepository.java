package com.wip.bool.domain.music;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    public List<SongDetail> findAll(SongMaster songMaster, SortType sortType,
                                    OrderType order, PageRequest pageRequest) {

        return queryFactory.selectFrom(QSongDetail.songDetail)
                .where(songMasterEq(songMaster))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getOrder(sortType, order))
                .fetchResults().getResults();
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
