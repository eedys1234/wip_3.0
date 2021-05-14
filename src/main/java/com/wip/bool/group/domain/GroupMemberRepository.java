package com.wip.bool.group.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public GroupMember save(GroupMember groupMember) {
        entityManager.persist(groupMember);
        return groupMember;
    }

    public Long delete(GroupMember groupMember) {
        entityManager.remove(groupMember);
        return 1L;
    }


}
