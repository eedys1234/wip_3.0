package com.wip.bool.music.guitar.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GuitarCodeRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public GuitarCode save(GuitarCode guitarCode) {
        entityManager.persist(guitarCode);
        return guitarCode;
    }

    public Optional<GuitarCode> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GuitarCode.class, id));
    }

    public List<GuitarCode> findAll() {
        return queryFactory.selectFrom(QGuitarCode.guitarCode)
                .fetch();
    }

    public Integer maxOrder() {
        return queryFactory.select(QGuitarCode.guitarCode.guitarOrder.max())
                .from(QGuitarCode.guitarCode)
                .fetchFirst();
    }

    public Long delete(GuitarCode guitarCode) {
        entityManager.remove(guitarCode);
        return 1L;
    }
}
