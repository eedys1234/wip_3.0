package com.wip.bool.domain.user;

import com.wip.bool.domain.cmmn.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
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
}
