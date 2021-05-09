package com.wip.bool.board.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.board.dto.ReplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wip.bool.board.domain.QImageFile.imageFile;
import static com.wip.bool.board.domain.QReply.reply;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Reply save(Reply reply) {
        entityManager.persist(reply);
        return reply;
    }

    public Optional<Reply> findById(Long replyId) {
        return Optional.ofNullable(
                queryFactory.select(reply)
                .from(reply)
                .where(reply.id.eq(replyId))
                .fetchOne()
        );
    }

    public Optional<Reply> findById(Long userId, Long replyId) {
        return Optional.ofNullable(
            queryFactory.select(reply)
                    .from(reply)
                    .where(reply.id.eq(replyId), reply.user.id.eq(userId))
                    .fetchOne()
        );
    }

    public List<ReplyDto.ReplyResponse> findAllByBoard(Long boardId, int size, int offset) {

        List<Reply> replies = queryFactory
                .select(reply)
                .from(reply)
                .leftJoin(reply.imageFiles, imageFile)
                .fetchJoin()
                .where(reply.board.id.eq(boardId))
                .orderBy(reply.createDate.asc())
                .offset(offset)
                .limit(size)
                .fetch();

        return replies.stream()
                .map(reply -> new ReplyDto.ReplyResponse(reply, reply.getImageFiles()))
                .collect(Collectors.toList());
    }

    public List<ReplyDto.ReplyResponse> findAllByReply(Long parentId, int size, int offset) {

        List<Reply> replies = queryFactory.select(reply)
                .from(reply)
                .leftJoin(reply.parentReply)
                .fetchJoin()
                .leftJoin(reply.imageFiles, imageFile)
                .fetchJoin()
                .where(reply.parentReply.id.eq(parentId))
                .orderBy(reply.parentReply.id.asc().nullsFirst(), reply.createDate.desc())
                .offset(offset)
                .limit(size)
                .fetch();

        return replies.stream()
                .map(reply -> new ReplyDto.ReplyResponse(reply, reply.getImageFiles()))
                .collect(Collectors.toList());
    }

    public Long delete(Reply reply) {
        entityManager.remove(reply);
        return 1L;
    }

}
