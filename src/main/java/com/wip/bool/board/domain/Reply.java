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
@Table(name = "reply")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_delete")
    private DeleteStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply")
    private List<ImageFile> imageFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parentReply;

    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL)
    private List<Reply> childReply = new ArrayList<>();

    public static Reply createReply(String content, Board board, User user) {
        Reply reply = new Reply();
        reply.updateContent(content);
        reply.updateUser(user);
        reply.updateBoard(board);
        reply.createStatus();
        return reply;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateBoard(Board board) {
        this.board = board;
    }

    public void updateParentReply(Reply parentReply) {
        this.parentReply = parentReply;
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
