package com.wip.bool.board.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.board.domain.QReply.reply;
import static com.wip.bool.cmmn.util.RoleUtils.isRoleAdmin;
import static com.wip.bool.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class    ReplyRepository {

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

    public Optional<Reply> findById(Long userId, Long replyId, Role role) {
        return Optional.ofNullable(
            queryFactory.select(reply)
                    .from(reply)
                    .where(reply.id.eq(replyId), eqAdmin(userId, role))
                    .fetchOne()
        );
    }

    public List<ReplyDto.ReplyResponse> findAllByBoard(Long boardId, int size, int offset) {

        return queryFactory
                .select(Projections.constructor(ReplyDto.ReplyResponse.class, reply))
                .from(reply)
                .innerJoin(reply.user, user)
                .fetchJoin()
                .where(reply.board.id.eq(boardId))
                .orderBy(reply.createDate.asc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public List<ReplyDto.ReplyResponse> findAllByReply(Long parentId, int size, int offset) {
        return queryFactory.select(Projections.constructor(ReplyDto.ReplyResponse.class, reply))
                .from(reply)
                .leftJoin(reply.parentReply)
                .fetchJoin()
                .innerJoin(reply.user, user)
                .fetchJoin()
                .where(reply.parentReply.id.eq(parentId))
                .orderBy(reply.parentReply.id.asc().nullsFirst(), reply.createDate.desc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public Long delete(Reply reply) {
        entityManager.remove(reply);
        return 1L;
    }

    private BooleanExpression eqAdmin(Long userId, Role role) {
        return !isRoleAdmin(role) ? reply.user.id.eq(userId) : null;
    }

}
