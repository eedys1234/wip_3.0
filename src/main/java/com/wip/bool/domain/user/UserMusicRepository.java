package com.wip.bool.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserMusicRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;


}
