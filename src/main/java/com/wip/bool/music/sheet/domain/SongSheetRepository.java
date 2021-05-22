package com.wip.bool.music.sheet.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.music.sheet.domain.QSongSheet.songSheet;

@Repository
@RequiredArgsConstructor
public class SongSheetRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public SongSheet save(SongSheet songSheet) {
        entityManager.persist(songSheet);
        return songSheet;
    }

    public List<SongSheet> findBySongDetail(Long songDetailId) {
        return queryFactory.selectFrom(songSheet)
                .where(songSheet.songDetail.id.eq(songDetailId))
                .orderBy(songSheet.sheetOrder.asc())
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
