package com.wip.bool.board.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageFileRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public ImageFile save(ImageFile imageFile) {
        entityManager.persist(imageFile);
        return imageFile;
    }

    public Optional<ImageFile> findById(Long imageFileId) {
        return Optional.ofNullable(entityManager.find(ImageFile.class, imageFileId));
    }

    public Long delete(ImageFile imageFile) {
        entityManager.remove(imageFile);
        return 1L;
    }
}
