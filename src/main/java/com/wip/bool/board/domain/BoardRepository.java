package com.wip.bool.board.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.board.dto.BoardDto;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.board.domain.QBoard.board;
import static com.wip.bool.cmmn.util.RoleUtils.isRoleAdmin;
import static com.wip.bool.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Board save(Board board) {
        entityManager.persist(board);
        return board;
    }

    public List<BoardDto.BoardSimpleResponse> findAll(BoardType boardType, int size, int offset) {
        return queryFactory.select(Projections.constructor(BoardDto.BoardSimpleResponse.class,
                board.id, board.title, board.isDeleted, board.boardType))
                .from(board)
                .where(board.boardType.eq(boardType))
                .orderBy(board.createDate.desc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(
                queryFactory.select(board)
                        .from(board)
                        .where(board.id.eq(boardId))
                        .fetchOne()
        );
    }

    public Optional<Board> findById(Long userId, Long boardId, Role role) {
        return Optional.ofNullable(
                queryFactory.select(board)
                        .from(board)
                        .where(eqAdmin(userId, role), board.id.eq(boardId))
                        .fetchOne()
        );
    }

    public Optional<BoardDto.BoardResponse> findDetailById(Long boardId) {
        Board boardEntity = queryFactory
                            .select(board)
                            .from(board)
                            .innerJoin(board.user, user)
                            .where(board.id.eq(boardId))
                            .fetchOne();

        return Optional.ofNullable(new BoardDto.BoardResponse(boardEntity));
    }

    public Long delete(Board board) {
        entityManager.remove(board);
        return 1L;
    }

    private BooleanExpression eqAdmin(Long userId, Role role) {
        return !isRoleAdmin(role) ? board.user.id.eq(userId) : null;
    }
}
