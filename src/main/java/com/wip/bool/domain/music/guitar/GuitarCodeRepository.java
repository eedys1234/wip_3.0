package com.wip.bool.domain.music.guitar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.domain.music.QGuitarCode;
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

}
