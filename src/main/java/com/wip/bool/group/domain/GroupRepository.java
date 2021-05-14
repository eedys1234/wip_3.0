package com.wip.bool.group.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.group.domain.QGroup.group;
import static com.wip.bool.group.domain.QGroupMember.groupMember;
import static com.wip.bool.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Group save(Group group) {
        entityManager.persist(group);
        return group;
    }

    public Optional<Group> findById(Long groupId) {
        return Optional.ofNullable(entityManager.find(Group.class, groupId));
    }

    public Optional<Group> findById(Long usrId, Long groupId) {
        return Optional.ofNullable(
                    queryFactory.selectFrom(group)
                    .where(group.id.eq(groupId), group.groupMaster.id.eq(usrId))
                    .fetchOne()
                );
    }

    public List<Group> findAllByMaster(Long userId) {
        return queryFactory.selectFrom(group)
                .leftJoin(group.groupMembers, groupMember)
                .fetchJoin()
                .where(group.groupMaster.id.eq(userId))
                .fetch();
    }

    public List<Group> findAllByUser(Long userId) {
        return queryFactory.select(group)
                .from(group)
                .innerJoin(group.groupMembers, groupMember)
                .fetchJoin()
                .innerJoin(group.groupMaster, user)
                .fetchJoin()
                .where(groupMember.user.id.eq(userId))
                .fetch();
    }

    public Long delete(Group group) {
        entityManager.remove(group);
        return 1L;
    }
}
