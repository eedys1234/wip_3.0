package com.wip.bool.userbox.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.cmmn.type.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    public Optional<UserBox> findById(Long userId, Long userBoxId) {
        return Optional.ofNullable(
                    queryFactory.selectFrom(userBox)
                        .where(userBox.id.eq(userBoxId), userBox.user.id.eq(userId))
                        .fetchOne()
                );
    }

    public List<UserBox> findAll(OrderType orderType, int size, int offset, Long authorityId) {
        return queryFactory.select(userBox)
                .from(userBox)
                .innerJoin(rights)
                .on(userBox.id.eq(rights.targetId), rights.authorityId.eq(authorityId))
                .orderBy(getOrder(orderType))
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<UserBox> findAll(OrderType orderType, int size, int offset, List<Long> authorityId) {
        return queryFactory.selectFrom(userBox)
                .innerJoin(rights)
                .on(userBox.id.eq(rights.targetId))
                .where(rights.authorityId.in(authorityId))
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
}
