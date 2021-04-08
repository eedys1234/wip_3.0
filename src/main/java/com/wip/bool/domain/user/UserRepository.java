package com.wip.bool.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(QUser.user)
                .where(QUser.user.id.eq(id))
                .fetchOne());
    }

    public List<User> findAll() {
        return queryFactory.selectFrom(QUser.user)
                .fetch();
    }

    public User findByUserId(String userId) {

        return queryFactory.selectFrom(QUser.user)
                .where(QUser.user.userId.eq(userId))
                .fetchOne();
    }

    public int delete(Long id) {
        User user = entityManager.find(User.class, id);
        if(!Objects.isNull(user)) {
            return delete(user);
        }

        return 0;
    }

    public int delete(User user) {
        entityManager.remove(user);
        return 1;
    }
}
