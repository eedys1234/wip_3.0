package com.wip.bool.rights.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.auth.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.wip.bool.rights.domain.QRights.rights;

@Repository
@RequiredArgsConstructor
public class RightsRepositoryImpl {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public List<Rights> findByUserBox(Long targetId, List<Long> authorityIds) {
        return queryFactory.select(rights)
                .from(rights)
                .where(rights.authorityId.in(authorityIds), rights.targetId.eq(targetId), rights.target.eq(Target.USERBOX))
                .fetch();
    }

}
