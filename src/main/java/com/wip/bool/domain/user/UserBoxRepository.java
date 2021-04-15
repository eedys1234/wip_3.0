package com.wip.bool.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.wip.bool.domain.user.QUserBox.userBox;

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
        return queryFactory.selectFrom(userBox)
                            .where(userBox.user.id.eq(userId))
                            .fetch();
    }

    public int delete(UserBox userBox) {
        entityManager.remove(userBox);
        return 1;
    }

    public int delete(Long id) {
        UserBox userBox = entityManager.find(UserBox.class, id);

        if(!Objects.isNull(userBox)) {
            return delete(userBox);
        }

        return 0;
    }
}
