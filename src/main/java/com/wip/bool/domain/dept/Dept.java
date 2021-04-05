package com.wip.bool.domain.dept;

import com.wip.bool.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Dept {

    @Id
    @GeneratedValue
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name")
    private String deptName;

    @OneToMany(mappedBy = "dept")
    private List<User> users = new ArrayList<>();
}
