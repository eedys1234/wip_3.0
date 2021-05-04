package com.wip.bool.board.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.board.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.board.domain.QBoard.board;
import static com.wip.bool.board.domain.QImageFile.imageFile;

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
                board.id, board.title, board.boardType))
                .from(board)
                .where(board.boardType.eq(boardType))
                .orderBy(board.createDate.asc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    public Optional<Board> findById(Long userId, Long boardId) {
        return Optional.ofNullable(
                queryFactory.select(board)
                .from(board)
                .where(board.user.id.eq(userId), board.id.eq(boardId))
                .fetchOne()
        );
    }

    public Optional<BoardDto.BoardResponse> findDetailById(Long boardId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(BoardDto.BoardResponse.class,
                        board.id, board.title, board.content, board.boardType,
                        imageFile.filePath, imageFile.newFileName, imageFile.imageFileExt))
                            .from(board)
                            .innerJoin(imageFile)
                            .on(board.imageFiles.contains(imageFile))
                            .where(board.id.eq(boardId))
                            .fetchOne()
        );
    }

    public Long delete(Board board) {
        entityManager.remove(board);
        return 1L;
    }


}