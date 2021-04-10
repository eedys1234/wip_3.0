package com.wip.bool.domain.user;

import com.wip.bool.domain.cmmn.BaseEntity;
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
public class UserBox extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_box_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private User user;

    private String userBoxName;

    @OneToMany(mappedBy = "userBox")
    private List<UserMusic> userMusics = new ArrayList<>();

    public static UserBox createUserBox(User user, String userBoxName) {
        UserBox userBox = new UserBox();
        userBox.setUser(user);
        userBox.updateUserBoxName(userBoxName);
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
}
