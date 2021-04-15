package com.wip.bool.domain.music;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SongMasterRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public SongMaster save(SongMaster songMaster) {
        entityManager.persist(songMaster);
        return songMaster;
    }

    public Optional<SongMaster> findById(Long id) {
        return Optional.ofNullable(entityManager.find(SongMaster.class, id));
    }

    public List<SongMaster> findAll(String order) {

        return queryFactory.selectFrom(QSongMaster.songMaster)
                .orderBy(codeOrder(order))
                .fetch();

    }

    private OrderSpecifier codeOrder(String order) {
        return "ASC".equals(order) ? QSongMaster.songMaster.codeOrder.asc()
                : QSongMaster.songMaster.codeOrder.desc();

    }

}
