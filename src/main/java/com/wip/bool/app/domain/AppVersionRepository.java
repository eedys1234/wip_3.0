package com.wip.bool.app.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.app.domain.QAppVersion.appVersion;

@Repository
@RequiredArgsConstructor
public class AppVersionRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public AppVersion save(AppVersion appVersion) {
        entityManager.persist(appVersion);
        return appVersion;
    }

    public Optional<AppVersion> findOne(String name) {
        return Optional.ofNullable(
                queryFactory.selectFrom(appVersion)
                            .where(appVersion.name.eq(name))
                            .fetchOne()
        );
    }

    public Optional<AppVersion> findById(Long appVersionid) {
        return Optional.ofNullable(
                queryFactory.selectFrom(appVersion)
                        .where(appVersion.id.eq(appVersionid))
                        .fetchOne()
        );
    }

    public List<AppVersion> findAll() {
        return queryFactory
                .selectFrom(appVersion)
                .fetch();
    }

    public Long delete(AppVersion appVersion) {
        entityManager.remove(appVersion);
        return 1L;
    }
}
