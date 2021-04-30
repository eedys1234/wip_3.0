package com.wip.bool.music.mp3.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.music.song.domain.SongDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.wip.bool.music.mp3.domain.QSongMP3.songMP3;

@Repository
@RequiredArgsConstructor
public class SongMP3Repository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public SongMP3 save(SongMP3 songMP3) {
        entityManager.persist(songMP3);
        return songMP3;
    }

    public Long delete(SongMP3 songMP3) {
        entityManager.remove(songMP3);
        return 1L;
    }

    public Optional<SongMP3> findById(Long songMP3Id) {
        return Optional.ofNullable(entityManager.find(SongMP3.class, songMP3Id));
    }

    public SongMP3 findBySongDetail(SongDetail songDetail) {
        return queryFactory.select(songMP3)
                .from(songMP3)
                .where(songMP3.songDetail.eq(songDetail))
                .fetchOne();
    }
}
