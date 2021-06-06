package com.wip.bool.group.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.cmmn.util.RoleUtils.isRoleAdmin;
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

    public Optional<Group> findById(Long userId, Long groupId, Role role) {
        return Optional.ofNullable(
                queryFactory.selectFrom(group)
                        .where(group.id.eq(groupId), eqAdmin(userId, role))
                        .fetchOne()
        );
    }

    public List<Group> findAllByMaster(Long userId, OrderType orderType, int size, int offset) {
        return queryFactory.selectFrom(group)
                .innerJoin(group.user, user)
                .fetchJoin()
                .where(group.user.id.eq(userId))
                .orderBy(getOrder(orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<Group> findAllByUser(List<Long> groupIds, OrderType orderType, int size, int offset) {
        return queryFactory.select(group)
                .from(group)
                .innerJoin(group.user, user)
                .fetchJoin()
                .where(group.id.in(groupIds))
                .orderBy(getOrder(orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<Group> findAllByUser(Long userId) {
        return queryFactory.select(group)
                .from(group)
                .innerJoin(group.groupMembers, groupMember)
                .fetchJoin()
                .innerJoin(group.user, user)
                .fetchJoin()
                .where(groupMember.user.id.eq(userId))
                .fetch();
    }


    public Long delete(Group group) {
        entityManager.remove(group);
        return 1L;
    }

    private OrderSpecifier getOrder(OrderType orderType) {
        return orderType == OrderType.ASC ? group.createDate.asc() :  group.createDate.desc();
    }


    private BooleanExpression eqAdmin(Long userId, Role role) {
        return !isRoleAdmin(role) ? group.user.id.eq(userId) : null;
    }
}
