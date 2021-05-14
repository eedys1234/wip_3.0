package com.wip.bool.userbox.domain;

import com.wip.bool.cmmn.BaseEntity;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_box")
public class UserBox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_box_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_box_name", nullable = false, length = 25)
    private String userBoxName;

    @OneToMany(mappedBy = "userBox")
    private List<UserBoxSong> userMusics = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "share_type", nullable = false)
    private ShareType shareType;

    public static UserBox createUserBox(User user, String userBoxName, ShareType shareType) {
        UserBox userBox = new UserBox();
        userBox.setUser(user);
        userBox.updateUserBoxName(userBoxName);
        userBox.updateShareType(shareType);
        return userBox;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateUserBoxName(String userBoxName) {
        if(!Objects.isNull(userBoxName)) {
            this.userBoxName = userBoxName;
        }
    }

    public void updateShareType(ShareType shareType) {
        this.shareType = shareType;
    }
}
