package com.wip.bool.userbox.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.auth.Target;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.user.domain.Role;
import com.wip.bool.userbox.dto.UserBoxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.cmmn.util.RoleUtils.isRoleAdmin;
import static com.wip.bool.rights.domain.QRights.rights;
import static com.wip.bool.userbox.domain.QUserBox.userBox;

@Repository
@RequiredArgsConstructor
public class UserBoxRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public UserBox save(UserBox userBox) {
        entityManager.persist(userBox);
        return userBox;
    }

    public Optional<UserBox> findById(Long userBoxId) {
        return Optional.ofNullable(entityManager.find(UserBox.class, userBoxId));
    }

    public Optional<UserBox> findById(Long userId, Long userBoxId, Role role) {
        return Optional.ofNullable(
                    queryFactory.selectFrom(userBox)
                        .where(userBox.id.eq(userBoxId), eqAdmin(userId, role))
                        .fetchOne()
                );
    }

    public List<UserBoxDto.UserBoxResponse> findAll(OrderType orderType, int size, int offset, Long authorityId) {
        return queryFactory.select(Projections.constructor(UserBoxDto.UserBoxResponse.class,
                userBox, rights.rightType))
                .from(userBox)
                .innerJoin(rights)
                .on(userBox.id.eq(rights.targetId), rights.target.eq(Target.USERBOX), rights.authorityId.eq(authorityId))
                .orderBy(getOrder(orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<UserBoxDto.UserBoxResponse> findAll(OrderType orderType, int size, int offset, List<Long> authorityId) {
        return queryFactory.select(Projections.constructor(UserBoxDto.UserBoxResponse.class,
                userBox, rights.rightType))
                .from(userBox)
                .innerJoin(rights)
                .on(userBox.id.eq(rights.targetId), rights.target.eq(Target.USERBOX), rights.authorityId.in(authorityId))
                .orderBy(getOrder(orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public Long delete(UserBox userBox) {
        entityManager.remove(userBox);
        return 1L;
    }

    private OrderSpecifier getOrder(OrderType orderType) {
        return orderType == OrderType.ASC ? userBox.createDate.asc() :  userBox.createDate.desc();
    }

    private BooleanExpression eqAdmin(Long userId, Role role) {
        return !isRoleAdmin(role) ? userBox.user.id.eq(userId) : null;
    }
}
