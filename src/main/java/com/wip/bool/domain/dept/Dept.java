package com.wip.bool.domain.dept;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

//    @OneToMany(mappedBy = "dept")
//    private List<User> users = new ArrayList<>();

    @Builder
    public Dept(String deptName) {
        this.deptName = deptName;
    }
}
