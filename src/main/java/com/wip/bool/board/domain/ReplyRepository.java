package com.wip.bool.board.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

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

    public List<Reply> findAllByBoard(Long boardId, int size, int offset) {
        return queryFactory.select(reply)
                .from(reply)
                .where(reply.board.id.eq(boardId))
                .orderBy(reply.createDate.asc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

//    public List<ReplyDto.ReplyResponse> findAllByReply(Long parentId) {
//    }

    public Long delete(Reply reply) {
        entityManager.remove(reply);
        return 1L;
    }
}
