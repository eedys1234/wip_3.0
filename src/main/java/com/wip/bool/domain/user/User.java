package com.wip.bool.domain.user;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.position.Position;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserConfig userConfig;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserBox> musicBoxes = new ArrayList<>();

    protected User() {
    }

    public static User createUser(Dept dept, Position position, Role role, UserConfig userConfig) {
        User user = new User();
        user.init(dept, position, role, userConfig);
        return user;
    }

    public User updatePassword(String userPassword) {
        if(!Objects.isNull(userPassword)) {
            this.userPassword = userPassword;
        }
        return this;
    }

    public User updateDept(Dept dept) {
        this.dept = dept;
        return this;
    }

    public User updatePosition(Position position) {
        this.position = position;
        return this;
    }

    public void approve() {
        this.role = Role.NOMARL;
    }

    private void init(Dept dept, Position position, Role role, UserConfig userConfig) {
        this.dept = dept;
        this.position = position;
        this.role = role;
        this.userConfig = userConfig;
    }
}
