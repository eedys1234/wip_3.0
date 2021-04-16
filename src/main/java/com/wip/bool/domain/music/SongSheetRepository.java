package com.wip.bool.domain.music;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SongSheetRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public SongSheet save(SongSheet songSheet) {
        entityManager.persist(songSheet);
        return songSheet;
    }

    public List<SongSheet> findBySongDetail(SongDetail songDetail) {
        return queryFactory.selectFrom(QSongSheet.songSheet)
                .where(QSongSheet.songSheet.songDetail.eq(songDetail))
                .fetch();
    }

    public Long delete(SongSheet songSheet) {
        entityManager.remove(songSheet);
        return 1L;
    }

    public Optional<SongSheet> findById(Long songSheetId) {
        return Optional.ofNullable(entityManager.find(SongSheet.class, songSheetId));
    }


 }
