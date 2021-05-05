package com.wip.bool.board.domain;

import com.wip.bool.cmmn.BaseEntity;
import com.wip.bool.cmmn.status.DeleteStatus;
import com.wip.bool.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false, unique = true, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_delete")
    private DeleteStatus isDeleted;

    @OneToMany(mappedBy = "board")
    private List<ImageFile> imageFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    public static Board createBoard(String title, String content, BoardType boardType, User user) {
        Board board = new Board();
        board.updateTitle(title);
        board.updateContent(content);
        board.updateBoardType(boardType);
        board.updateUser(user);
        board.createStatus();
        return board;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateBoardType(BoardType boardType) {
        this.boardType = boardType;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateImageFiles(List<ImageFile> imageFiles) {
        this.imageFiles.addAll(imageFiles);
    }

    public void createStatus() {
        this.isDeleted = DeleteStatus.NORMAL;
    }

    public void hiddenStatus() {
        this.isDeleted = DeleteStatus.HIDDEN;
    }

    public void deleteStatus() {
        this.isDeleted = DeleteStatus.DELETE;
    }
}
