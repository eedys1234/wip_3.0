package com.wip.bool.bible.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WordsMasterRepository {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public WordsMaster save(WordsMaster wordsMaster) {
        entityManager.persist(wordsMaster);
        return wordsMaster;
    }

    public Optional<WordsMaster> findById(Long id) {
        return Optional.ofNullable(entityManager.find(WordsMaster.class, id));
    }

    public List<WordsMaster> findAll() {
        return queryFactory.selectFrom(QWordsMaster.wordsMaster)
                .fetch();
    }
}