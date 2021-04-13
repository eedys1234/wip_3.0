package com.wip.bool.domain.music;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
