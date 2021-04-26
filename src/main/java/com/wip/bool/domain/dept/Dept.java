package com.wip.bool.domain.dept;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name", nullable = false, length = 2)
    private String deptName;

    @Builder
    public Dept(String deptName) {
        this.deptName = deptName;
    }

    public void update(String deptName) {
        if(!Objects.isNull(deptName)) {
            this.deptName = deptName;
        }
    }

}
