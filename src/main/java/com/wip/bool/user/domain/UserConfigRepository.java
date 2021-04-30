package com.wip.bool.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserConfigRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public UserConfig save(UserConfig userConfig) {
        entityManager.persist(userConfig);
        return userConfig;
    }

    public Optional<UserConfig> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserConfig.class, id));
    }

}
