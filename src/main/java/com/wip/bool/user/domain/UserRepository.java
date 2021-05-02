package com.wip.bool.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.user.domain.QUser.user;
import static com.wip.bool.dept.domain.QDept.dept;

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
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    public List<User> findAllByRole(Role role) {
        return queryFactory.selectFrom(user)
                .where(user.role.eq(role))
                .fetch();
    }

    public List<User> findAll() {
        return queryFactory.selectFrom(user)
                .fetch();
    }

    public Long login(String email, String userPassword) {

        return queryFactory.selectFrom(user)
                .where(user.email.eq(email)
                        .and(user.userPassword.eq(userPassword))
                        .and(user.role.in(Role.ROLE_NORMAL, Role.ROLE_ADMIN)))
                .fetchCount();
    }

    public Optional<User> findByEmail(String email) {

        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne());
    }

    public Long delete(User user) {
        entityManager.remove(user);
        return 1L;
    }

    public Optional<User> deptByUser(Long userId) {
        return Optional.ofNullable(
                queryFactory.select(user)
                            .from(user)
                            .innerJoin(dept)
                            .fetchJoin()
                            .where(user.id.eq(userId))
                            .fetchOne());
    }

    public List<Long> usersByDept(Long deptId) {
        return queryFactory.select(user.id)
                .from(user)
                .where(user.dept.id.eq(deptId))
                .fetch();
    }
}
