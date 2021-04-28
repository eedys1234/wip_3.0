package com.wip.bool.domain.userbox;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.domain.user.QUserBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserBoxRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public UserBox save(UserBox userBox) {
        entityManager.persist(userBox);
        return userBox;
    }

    public Optional<UserBox> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserBox.class, id));
    }

    public List<UserBox> findAllByUserId(Long userId) {
        return queryFactory.selectFrom(QUserBox.userBox)
                            .where(QUserBox.userBox.user.id.eq(userId))
                            .fetch();
    }

    public Long delete(UserBox userBox) {
        entityManager.remove(userBox);
        return 1L;
    }
}
