package com.wip.bool.group.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.group.domain.QGroupMember.groupMember;
import static com.wip.bool.user.domain.QUser.user;

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

    public Optional<GroupMember> findById(Long groupMemberId) {
        return Optional.ofNullable(
            entityManager.find(GroupMember.class, groupMemberId)
        );
    }

    public Optional<GroupMember> findById(Long userId, Long groupMemberId) {
        return Optional.ofNullable(
               queryFactory.selectFrom(groupMember)
               .where(groupMember.user.id.eq(userId), groupMember.id.eq(groupMemberId))
                .fetchOne()
        );
    }

    public List<GroupMember> findAllByGroup(Long groupId) {
        return queryFactory.selectFrom(groupMember)
                .innerJoin(groupMember.user, user)
                .fetchJoin()
                .where(groupMember.group.id.eq(groupId))
                .fetch();
   }
}
