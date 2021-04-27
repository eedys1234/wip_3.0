package com.wip.bool.domain.user;

import com.wip.bool.domain.cmmn.BaseEntity;
import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.position.Position;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", length = 10)
    private String name;

    @Column(name = "user_email", unique = true, length = 30, nullable = false)
    private String email;

    @Column(name = "user_password", length = 40)
    private String userPassword;

    @Column(name = "user_profile", length = 50)
    private String profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 10)
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserConfig userConfig;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserBox> musicBoxes = new ArrayList<>();

    public static User createUser(String email, String name, String profile, Role role) {
        User user = new User();
        return user.init(email, name, profile, role);
    }

    public static User createUser(String email, String name, String userPassword, String profile, Role role) {
        User user = new User();
        return user.init(email, name, userPassword, profile, role);
    }

    public User init(String email, String name, String profile, Role role) {

        this.email = email;
        this.name = name;
        this.profile = profile;
        this.role = role;

        return this;
    }

    public User init(String email, String name, String userPassword, String profile, Role role) {

        this.email = email;
        this.name = name;
        this.userPassword = userPassword;
        this.profile = profile;
        this.role = role;

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

    public User updateUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
        return this;
    }

    public User updateInfo(String name, String profile) {

        this.name = name;
        this.profile = profile;
        return this;
    }

    public User approve() {
        this.role = Role.NORMAL;
        return updateUserConfig(UserConfig.createUserConfig());
    }

//    private void init(Dept dept, Position position, Role role, UserConfig userConfig) {
//        this.dept = dept;
//        this.position = position;
//        this.role = role;
//        this.userConfig = userConfig;
//    }
}
