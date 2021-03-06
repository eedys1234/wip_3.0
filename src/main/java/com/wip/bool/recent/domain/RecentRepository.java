package com.wip.bool.recent.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.recent.dto.RecentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wip.bool.recent.domain.QRecent.recent;
import static com.wip.bool.music.song.domain.QSongDetail.songDetail;

@Repository
@RequiredArgsConstructor
public class RecentRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Recent save(Recent recent) {
        entityManager.persist(recent);
        return recent;
    }

    public List<RecentDto.RecentResponse> findAll(Long userId, int size, int offset) {

        return queryFactory.select(Projections.constructor(RecentDto.RecentResponse.class,
                recent.id, songDetail.id, songDetail.title, recent.createDate))
                .from(recent)
                .innerJoin(recent.songDetail, songDetail)
                .where(recent.user.id.eq(userId))
                .orderBy(recent.createDate.desc())
                .offset(offset)
                .limit(size)
                .fetch();
    }
}
