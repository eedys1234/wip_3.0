package com.wip.bool.domain.user;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.position.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_key")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "position_order")
    private int positionOrder;

    @OneToMany
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany
    private List<RecentSearch> recentSearches = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private UserConfig userConfig;

    @OneToMany
    private List<UserBox> musicBoxes = new ArrayList<>();
}
